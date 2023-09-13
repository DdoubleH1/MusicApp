package com.example.baseproject.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import java.io.File

class AudioUtils {

    fun getTrackArtUri(context: Context, audioUri: Uri): Uri? {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, audioUri)
            val embeddedArt = retriever.embeddedPicture
            if (embeddedArt != null) {
                // You can save the image to a file and return the URI of that file
                // For this example, let's assume you're saving the image as a temporary file
                val tempArtFile = File.createTempFile("track_art", ".jpg", context.cacheDir)
                tempArtFile.writeBytes(embeddedArt)
                return Uri.fromFile(tempArtFile)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }
        return null
    }
}
