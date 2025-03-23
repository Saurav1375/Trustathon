package com.example.trustoken_starter.auth.data.service

import android.net.Uri
import com.example.trustoken_starter.auth.domain.model.SignInResult
import com.example.trustoken_starter.auth.domain.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AuthWithUserPass (
    private val firestore: FirebaseFirestore
    ) {

    private val auth: FirebaseAuth = Firebase.auth
    suspend fun signUp(emailId: String, password: String, name: String): SignInResult = try {
        val user = auth.createUserWithEmailAndPassword(emailId, password).await().user
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("profile_images/guest_image.jpg")
        val downloadUrl = storageRef.downloadUrl.await()
        user?.let {
            val profileUpdates = userProfileChangeRequest {
                displayName = name
                this.photoUri = Uri.parse(downloadUrl.toString())
            }
            it.updateProfile(profileUpdates).await()
        }
        user?.let {
            val userData = UserData(
                userId = it.uid,
                username = name,
                profilePictureUrl = it.photoUrl?.toString(),
                emailId = it.email.toString()
            )

            saveUserToFirestore(userData)
        }
        println("email: $emailId, pass: $password")

        SignInResult(
            data = user?.run {
                UserData(
                    userId = uid,
                    username = displayName,
                    profilePictureUrl = photoUrl?.toString(),
                    emailId = email.toString()
                )
            },
            errorMessage = null

        )


    } catch (e: Exception) {
        e.printStackTrace()
        SignInResult(data = null, errorMessage = e.message)
    }

    suspend fun signIn(emailId: String, password: String): SignInResult = try {
        val user = auth.signInWithEmailAndPassword(emailId, password).await().user
        SignInResult(
            data = user?.run {
                UserData(
                    userId = uid,
                    username = displayName,
                    profilePictureUrl = photoUrl?.toString(),
                    emailId = email.toString()
                )
            },
            errorMessage = null
        )
    } catch (e: Exception) {
        e.printStackTrace()
        println(e.message)
        println("email: $emailId, pass: $password")
        SignInResult(data = null, errorMessage = e.message ?: "An unknown error occurred.")
    }


    private suspend fun saveUserToFirestore(user: UserData) {

        try {
            firestore.collection("users")
                .document(user.emailId) // Use userId as document ID
                .set(user) // Save entire user object
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e // Re-throw if you want to handle it at a higher level
        }
    }



    fun sendPasswordResetEmail(email: String, callback: (Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }
}

