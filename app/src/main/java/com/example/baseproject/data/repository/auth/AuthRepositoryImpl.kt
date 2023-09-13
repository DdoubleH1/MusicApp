package com.example.baseproject.data.repository.auth

import android.util.Log
import com.example.baseproject.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl : AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override suspend fun firebaseSignUp(
        email: String,
        password: String,
        userName: String
    ): Response<Boolean> {
        Log.e("AuthRepositoryImpl", "firebaseSignUp: $email $password $userName")
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            database.reference.child("users")
                .child(auth.currentUser!!.uid)
                .child("profile")
                .apply {
                    child("email").setValue(email)
                    child("display_name").setValue(userName)
                }
            Log.e("AuthRepositoryImpl", "firebaseSignUp: Success"   )
            Response.Success(true)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "firebaseSignUp: ${e.message}")
            Response.Failure(e)
        }
    }

    override suspend fun firebaseLogin(email: String, password: String): Response<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Log.e("AuthRepositoryImpl", "firebaseSignIn: Success"   )
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun firebaseLogout(): Response<Boolean> {
        return try {
            auth.signOut()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override fun isLogin(): Boolean {
        return auth.currentUser != null
    }
}