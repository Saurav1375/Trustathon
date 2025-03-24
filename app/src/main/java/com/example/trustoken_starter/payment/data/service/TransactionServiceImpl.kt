package com.example.trustoken_starter.payment.data.service

import android.app.Application
import android.widget.Toast
import com.example.trustoken_starter.payment.domain.model.TransactionEntity
import com.example.trustoken_starter.payment.domain.service.TransactionService
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Implementation of the TransactionService that interacts with Firebase Realtime Database.
 * Provides methods to save, retrieve, and observe transactions in real-time.
 */
class TransactionServiceImpl(
    private val db: FirebaseDatabase,
    private val application: Application
) : TransactionService {

    /**
     * Saves a transaction to Firebase Realtime Database.
     *
     * @param transaction The transaction entity to be saved.
     * @return True if the transaction is successfully saved, false otherwise.
     */
    override suspend fun saveTransaction(transaction: TransactionEntity): Boolean {
        val tt = TransactionParser.toJson(transaction)
        return try {
            val database = FirebaseDatabase.getInstance().reference

            // Reference to the transaction node in the database
            val transactionRef = database.child("transactions").child(transaction.id)
            transactionRef.setValue(tt).await()

            true
        } catch (e: Exception) {
            println("Failed to save transaction: ${e.message}")
            false
        }
    }

    // Reference to the "transactions" node in Firebase
    private val transactionsRef = db.getReference("transactions")

    /**
     * Retrieves all transactions as a Flow, providing real-time updates.
     *
     * @return A Flow that emits a list of TransactionEntity objects whenever there is a change.
     */
    override fun getAllTransactionsAsFlow(): Flow<List<TransactionEntity>> = callbackFlow {
        val transactionsList = mutableListOf<TransactionEntity>()

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transactionsList.clear()

                for (transactionSnapshot in snapshot.children) {
                    val transaction = transactionSnapshot.getValue(String::class.java)
                    transaction?.let { transactionsList.add(TransactionParser.fromJson(it)) }
                }

                trySend(transactionsList.toList()) // Emit updated transaction list
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException()) // Close Flow in case of an error
            }
        }

        transactionsRef.addValueEventListener(listener)

        // Remove the listener when the Flow collection is stopped
        awaitClose {
            transactionsRef.removeEventListener(listener)
        }
    }

    /**
     * Retrieves a single transaction by its ID as a Flow, providing real-time updates.
     *
     * @param transactionId The unique ID of the transaction.
     * @return A Flow that emits the requested TransactionEntity whenever there is a change.
     */
    override fun getTransactionByIdAsFlow(transactionId: String): Flow<TransactionEntity?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transaction = snapshot.getValue(String::class.java)
                trySend(transaction?.let { TransactionParser.fromJson(it) }) // Emit transaction data
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException()) // Close Flow in case of an error
            }
        }

        val transactionRef = transactionsRef.child(transactionId)
        transactionRef.addValueEventListener(listener)

        // Remove the listener when the Flow collection is stopped
        awaitClose {
            transactionRef.removeEventListener(listener)
        }
    }

    /**
     * Fetches a transaction by its ID once (one-time retrieval, not real-time).
     *
     * @param transactionId The unique ID of the transaction.
     * @return The requested TransactionEntity, or null if not found.
     */
    override suspend fun getTransactionById(transactionId: String): TransactionEntity? {
        return try {
            val snapshot = transactionsRef.child(transactionId).get().await()
            val transaction = snapshot.getValue(String::class.java)
            if (transaction != null) {
                TransactionParser.fromJson(transaction) // Parse and return the transaction
            }
            null
        } catch (e: Exception) {
            null
        }
    }
}
