package com.example.trustoken_starter.auth.data.service

import com.example.trustoken_starter.auth.domain.model.UserData
import com.example.trustoken_starter.auth.domain.repository.UserWalletService
import com.example.trustoken_starter.payment.domain.Wallet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
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
    override suspend fun getCurrentUser(): UserData? {
        val userId = auth.currentUser?.uid ?: return null

        return try {
            val doc = firestore.collection("users").document(userId).get().await()
            doc.toObject(UserData::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Fetch the current user's wallet data from Firestore.
     */
    override suspend fun getCurrentUserWallet(): Wallet? {
        val userId = auth.currentUser?.uid ?: return null

        return try {
            val doc = firestore.collection("wallets").document(userId).get().await()
            doc.toObject(Wallet::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    override suspend fun getAllUsers(): Flow<List<UserData>> = callbackFlow {
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
}
