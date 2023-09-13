package com.example.baseproject.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nonnull

@Entity(tableName = "playlist_data")
data class LibraryItem(
    @Nonnull
    @PrimaryKey
    val playlistId: String = "",
    var playlistTitle: String? = null,
    var playlistImage: String? = null
) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(playlistId)
        dest.writeString(playlistTitle)
        dest.writeString(playlistImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LibraryItem> {
        override fun createFromParcel(parcel: Parcel): LibraryItem {
            return LibraryItem(parcel)
        }

        override fun newArray(size: Int): Array<LibraryItem?> {
            return arrayOfNulls(size)
        }
    }
}
