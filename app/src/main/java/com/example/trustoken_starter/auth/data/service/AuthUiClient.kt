package com.example.trustoken_starter.auth.data.service

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.trustoken_starter.R
import com.example.trustoken_starter.auth.domain.model.SignInResult
import com.example.trustoken_starter.auth.domain.model.UserData
import com.example.trustoken_starter.auth.domain.utils.generateSecureRandomNumber
import com.example.trustoken_starter.payment.domain.model.Wallet
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class AuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient,
    private val firestore: FirebaseFirestore
) {

    private val auth = Firebase.auth
    // Google sign-in logic
    suspend fun SignIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(buildSignInRequest()).await()

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }


    suspend fun SignInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredential).await().user
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
                data = user.run {
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
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }


    // Sign out logic for both Google and Facebook
    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString(),
            emailId = email.toString()
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .build()
    }
}
