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

class AuthWithUserPass(private val firestore: FirebaseFirestore) {

    private val auth: FirebaseAuth = Firebase.auth
    private val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/guest_image.jpg")

    /**
     * Signs up a new user, assigns a wallet, and saves their details in Firestore.
     */
    suspend fun signUp(emailId: String, password: String, name: String): SignInResult = runCatching {
        val user = auth.createUserWithEmailAndPassword(emailId, password).await().user
            ?: return SignInResult(null, "User registration failed")

        val profileImageUrl = storageRef.downloadUrl.await().toString()

        user.updateProfile(userProfileChangeRequest {
            displayName = name
            photoUri = Uri.parse(profileImageUrl)
        }).await()

        val userId = user.uid
        val userData = UserData(
            userId = userId,
            username = name,
            profilePictureUrl = profileImageUrl,
            emailId = emailId
        )

        saveUserToFirestore(userData)
        setupUserWallet(userId)

        SignInResult(data = userData, errorMessage = null)
    }.getOrElse { e ->
        e.printStackTrace()
        SignInResult(null, e.message ?: "An error occurred")
    }

    /**
     * Signs in a user using email and password.
     */
    suspend fun signIn(emailId: String, password: String): SignInResult = runCatching {
        val user = auth.signInWithEmailAndPassword(emailId, password).await().user
            ?: return SignInResult(null, "Invalid credentials")

        SignInResult(
            data = UserData(
                userId = user.uid,
                username = user.displayName,
                profilePictureUrl = user.photoUrl?.toString(),
                emailId = user.email ?: ""
            ),
            errorMessage = null
        )
    }.getOrElse { e ->
        e.printStackTrace()
        SignInResult(null, e.message ?: "An error occurred")
    }

    /**
     * Sends a password reset email.
     */
    fun sendPasswordResetEmail(email: String, callback: (Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task -> callback(task.isSuccessful) }
    }

    /**
     * Saves user details to Firestore.
     */
    private suspend fun saveUserToFirestore(user: UserData) = runCatching {
        firestore.collection("users")
            .document(user.userId)
            .set(user, SetOptions.merge())
            .await()
    }.onFailure { it.printStackTrace() }

    /**
     * Creates and assigns a wallet to the user if one does not exist.
     */
    private suspend fun setupUserWallet(userId: String) = runCatching {
        val walletRef = firestore.collection("wallets").document(userId)
        val walletSnapshot = walletRef.get().await()

        if (!walletSnapshot.exists()) {
            val wallet = Wallet(
                userId = userId,
                publicKey = "",
                walletAddress = generateSecureRandomNumber(),
                balance = 10_000.0,
                creationDate = System.currentTimeMillis()
            )
            walletRef.set(wallet).await()
        }
    }.onFailure { it.printStackTrace() }
}

