package com.example.carservice.db

import android.net.Uri
import com.example.carservice.models.Apartment
import com.google.firebase.storage.FirebaseStorage
import java.util.concurrent.CountDownLatch

object FireStorage {

    private val storageReference = FirebaseStorage.getInstance().reference

    fun uploadImageByUri(uri: Uri, countDownLatch: CountDownLatch, apartment: Apartment) {
        val currentImageReference = storageReference.child("images/${uri.lastPathSegment}")
        currentImageReference.putFile(uri)

        apartment.imagesPaths.add(currentImageReference.path)
        countDownLatch.countDown()
    }
}