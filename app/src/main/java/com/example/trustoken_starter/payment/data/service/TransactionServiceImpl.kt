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

class TransactionServiceImpl(
    private val db: FirebaseDatabase,
    private val application: Application
) : TransactionService {
    override suspend fun saveTransaction(transaction: TransactionEntity): Boolean {
        val tt = TransactionParser.toJson(transaction)
        return try {
            val database = FirebaseDatabase.getInstance().reference

            val transactionRef = database.child("transactions").child(transaction.id)
            transactionRef.setValue(tt).await()

            true
        } catch (e: Exception) {
            println("Failed to save transaction: ${e.message}")
            false
        }
    }


    private val transactionsRef = db.getReference("transactions")

    // Method 1: Get all transactions with real-time updates
    override fun getAllTransactionsAsFlow(): Flow<List<TransactionEntity>> = callbackFlow {
        val transactionsList = mutableListOf<TransactionEntity>()

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transactionsList.clear()

                for (transactionSnapshot in snapshot.children) {
                    val transaction = transactionSnapshot.getValue(String::class.java)
                    transaction?.let { transactionsList.add(TransactionParser.fromJson(it)) }
                }

                trySend(transactionsList.toList())
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        transactionsRef.addValueEventListener(listener)

        // Remove the listener when the flow is cancelled
        awaitClose {
            transactionsRef.removeEventListener(listener)
        }
    }

    // Method 2: Get a single transaction by ID with real-time updates
    override fun getTransactionByIdAsFlow(transactionId: String): Flow<TransactionEntity?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transaction = snapshot.getValue(String::class.java)
                trySend(transaction?.let { TransactionParser.fromJson(it)})
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        val transactionRef = transactionsRef.child(transactionId)
        transactionRef.addValueEventListener(listener)

        // Remove the listener when the flow is cancelled
        awaitClose {
            transactionRef.removeEventListener(listener)
        }
    }

    // Optional: A non-Flow version to get a transaction by ID (one-time fetch)
    override suspend fun getTransactionById(transactionId: String): TransactionEntity? {
        return try {
            val snapshot = transactionsRef.child(transactionId).get().await()
            val transaction = snapshot.getValue(String::class.java)
            if (transaction != null) {
                TransactionParser.fromJson(transaction)
            }
            null
        } catch (e: Exception) {
            null
        }
    }
}