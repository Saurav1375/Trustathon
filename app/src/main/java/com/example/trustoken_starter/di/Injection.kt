package com.example.trustoken_starter.di

import com.google.firebase.firestore.FirebaseFirestore


/**
 * Provides a singleton instance of Firebase Firestore for cloud database operations.
 */

object Injection{
    private val instance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    fun instance(): FirebaseFirestore {
        return instance
    }
}