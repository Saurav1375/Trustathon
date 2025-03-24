package com.example.trustoken_starter.payment.domain.service

import com.example.trustoken_starter.payment.domain.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface TransactionService {
    suspend fun saveTransaction(transaction: TransactionEntity) : Boolean
    fun getAllTransactionsAsFlow(): Flow<List<TransactionEntity>>
    fun getTransactionByIdAsFlow(transactionId: String): Flow<TransactionEntity?>
    suspend fun getTransactionById(transactionId: String): TransactionEntity?


}