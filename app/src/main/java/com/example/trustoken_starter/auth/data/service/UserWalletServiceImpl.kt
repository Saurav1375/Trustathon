package com.example.trustoken_starter.auth.data.service

import com.example.trustoken_starter.auth.domain.model.UserData
import com.example.trustoken_starter.auth.domain.repository.UserWalletService
import com.example.trustoken_starter.payment.domain.model.Wallet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserWalletServiceImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserWalletService {

    /**
     * Fetch the current user's data from Firestore.
     */
    /**
     * Fetch the current user's data in real-time using Flow.
     */
    override fun getCurrentUser(): Flow<UserData?> = callbackFlow {
        // Wait for auth to be ready
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                // Set up Firestore listener only when we have a valid user
                val listener = firestore.collection("users")
                    .document(userId)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            close(error)
                            return@addSnapshotListener
                        }
                        trySend(snapshot?.toObject(UserData::class.java))
                    }

                // Store the listener reference for cleanup
            } else {
                // No user is signed in, send null
                trySend(null)
            }
        }

        // Start listening for auth state changes
        auth.addAuthStateListener(authStateListener)

        // Remove listeners when flow is closed
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    /**
     * Fetch the current user's wallet data in real-time.
     */
    override fun getCurrentUserWallet(): Flow<Wallet?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                val listener = firestore.collection("wallets")
                    .document(userId)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            close(error)
                            return@addSnapshotListener
                        }
                        trySend(snapshot?.toObject(Wallet::class.java))
                    }
            } else {
                trySend(null)
            }
        }

        auth.addAuthStateListener(authStateListener)

        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    /**
     * Fetch a user's wallet by wallet address in real-time.
     */
    override fun getUserWalletByAddress(walletAddress: String): Flow<Wallet?> = callbackFlow {
        val listener = firestore.collection("wallets")
            .whereEqualTo("walletAddress", walletAddress)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null) // Send null to indicate failure
                    close(error) // Close Flow on error
                    return@addSnapshotListener
                }

                val wallet = snapshot?.documents?.firstOrNull()?.toObject(Wallet::class.java)
                trySend(wallet) // Emit wallet updates
            }

        awaitClose { listener.remove() } // Remove listener when Flow collection stops
    }


    override fun getAllUsers(): Flow<List<UserData>> = callbackFlow {
        val listenerRegistration = firestore.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val users = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(UserData::class.java)?.let { userData ->
                        UserData(
                            userId = userData.userId,
                            username = userData.username,
                            profilePictureUrl = userData.profilePictureUrl,
                            emailId = userData.emailId,
                            walletAddress = userData.walletAddress
                        )
                    }
                } ?: emptyList()

                trySend(users)
            }

        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun saveWallet(wallet: Wallet): Boolean {
        return try {
            firestore.collection("wallets")
                .document(wallet.userId) // Using walletAddress as the document ID
                .set(wallet)
                .await()

            println("✅ Wallet saved successfully: ${wallet.walletAddress}")
            true
        } catch (e: Exception) {
            println("❌ Failed to save wallet: ${e.message}")
            false
        }
    }
}
