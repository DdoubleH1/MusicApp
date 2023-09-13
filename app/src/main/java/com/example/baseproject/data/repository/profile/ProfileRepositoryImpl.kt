package com.example.baseproject.data.repository.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.data.model.User
import com.example.baseproject.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl : ProfileRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override suspend fun updateProfile(name: String, profilePictureUri: Uri?): Response<Boolean> {
        return try {
            var url: String? = null
            if (profilePictureUri != null) {
                val storageRef = storage.reference
                val fileRef = storageRef.child("profile_pictures/${auth.uid}")
                fileRef.putFile(profilePictureUri).await()
                url = fileRef.downloadUrl.await().toString()
            }
            database.reference.child("users").child(auth.uid!!).child("profile").apply {
                child("display_name").setValue(name)
                if (url != null) child("profile_picture").setValue(url)
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override fun getProfile(): MutableLiveData<Response<User>> {
        val profileResponse = MutableLiveData<Response<User>>()
        database.reference.child("users").child(auth.uid!!).child("profile")
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val user = User(
                        name = snapshot.child("display_name").value.toString(),
                        profilePictureUrl = snapshot.child("profile_picture").value.toString(),
                    )
                    profileResponse.postValue(Response.Success(user))
                }
                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    profileResponse.postValue(Response.Failure(error.toException()))
                }
            })
        return profileResponse
    }
}