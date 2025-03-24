package com.example.trustoken_starter.auth.data.service

import android.net.Uri
import com.example.trustoken_starter.auth.domain.model.SignInResult
import com.example.trustoken_starter.auth.domain.model.UserData
import com.example.trustoken_starter.auth.domain.utils.generateSecureRandomNumber
import com.example.trustoken_starter.payment.domain.model.Wallet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AuthWithUserPass (
    private val firestore: FirebaseFirestore
    ) {

    private val auth: FirebaseAuth = Firebase.auth
    suspend fun signUp(emailId: String, password: String, name: String): SignInResult {
        return try {
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
            val userId = user?.uid ?: return SignInResult(null, "User ID is null")

            val userRef = firestore.collection("users").document(userId)
            val walletRef = firestore.collection("wallets").document(userId)

            // Check if the user already exists
            val userSnapshot = userRef.get().await()
            val walletSnapshot = walletRef.get().await()

            // If the wallet doesn't exist, generate new wallet data
            val wallet = if (!walletSnapshot.exists()) {
                Wallet(
                    userId = userId,
                    publicKey = "",
                    walletAddress = generateSecureRandomNumber(),
                    balance = 10000.0,
                    creationDate = System.currentTimeMillis()
                ).also {
                    walletRef.set(it).await()
                }
            } else {
                walletSnapshot.toObject(Wallet::class.java)!!
            }

            // Store user details
            val userData = mapOf(
                "userId" to userId,
                "username" to (user.displayName ?: "Unknown"),
                "emailId" to (user.email ?: "No Email"),
                "profilePictureUrl" to (user.photoUrl?.toString() ?: ""),
                "walletAddress" to wallet.walletAddress
            )

            userRef.set(userData, SetOptions.merge()).await()

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

