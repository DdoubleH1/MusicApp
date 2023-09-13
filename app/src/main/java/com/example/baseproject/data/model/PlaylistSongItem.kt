package com.example.baseproject.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song_data")
data class PlaylistSongItem(
    @PrimaryKey(autoGenerate = true)
    var songId: Int,
    var songImage: String?,
    var songTitle: String?,
    var artists: String?,
    var resource: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(songId)
        parcel.writeString(songImage)
        parcel.writeString(songTitle)
        parcel.writeString(artists)
        parcel.writeString(resource)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaylistSongItem> {
        override fun createFromParcel(parcel: Parcel): PlaylistSongItem {
            return PlaylistSongItem(parcel)
        }

        override fun newArray(size: Int): Array<PlaylistSongItem?> {
            return arrayOfNulls(size)
        }
    }
}