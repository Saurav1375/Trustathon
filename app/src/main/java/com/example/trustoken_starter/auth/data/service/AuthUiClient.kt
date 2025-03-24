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

    /**
     * Initiates the Google One Tap sign-in flow.
     * Returns an [IntentSender] that can be used to prompt the user for sign-in.
     */
    suspend fun SignIn(): IntentSender? {
        return try {
            oneTapClient.beginSignIn(buildSignInRequest()).await()?.pendingIntent?.intentSender
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
    }

    /**
     * Handles the sign-in intent result, authenticates the user with Firebase,
     * and stores user details in Firestore.
     *
     * @param intent The intent received from the sign-in flow.
     * @return [SignInResult] containing user data or an error message.
     */
    suspend fun SignInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken ?: return SignInResult(null, "Invalid Google ID Token")
        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredential).await().user
                ?: return SignInResult(null, "User authentication failed")

            val userId = user.uid
            val userRef = firestore.collection("users").document(userId)
            val walletRef = firestore.collection("wallets").document(userId)

            // Fetch user and wallet data from Firestore
            val userSnapshot = userRef.get().await()
            val walletSnapshot = walletRef.get().await()

            // Create a new wallet if not already present
            val wallet = walletSnapshot.toObject(Wallet::class.java) ?: Wallet(
                userId = userId,
                publicKey = "",
                walletAddress = generateSecureRandomNumber(),
                balance = 10000.0,
                creationDate = System.currentTimeMillis()
            ).also {
                walletRef.set(it).await()
            }

            // Save or update user details in Firestore
            val userData = mapOf(
                "userId" to userId,
                "username" to (user.displayName ?: "Unknown"),
                "emailId" to (user.email ?: "No Email"),
                "profilePictureUrl" to (user.photoUrl?.toString() ?: ""),
                "walletAddress" to wallet.walletAddress
            )

            userRef.set(userData, SetOptions.merge()).await()

            // Return successful sign-in result
            SignInResult(
                data = UserData(
                    userId = userId,
                    username = user.displayName,
                    profilePictureUrl = user.photoUrl?.toString(),
                    emailId = user.email ?: ""
                ),
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(null, e.message)
        }
    }

    /**
     * Signs out the currently authenticated user from Firebase and Google One Tap.
     */
    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    /**
     * Retrieves the currently signed-in user's details.
     *
     * @return [UserData] of the authenticated user, or null if no user is signed in.
     */
    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString(),
            emailId = email ?: ""
        )
    }

    /**
     * Builds a Google One Tap sign-in request.
     *
     * @return [BeginSignInRequest] configured for Google authentication.
     */
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

