package com.example.trustoken_starter.payment.data.service

import com.example.trustoken_starter.payment.domain.model.TransactionEntity
import kotlinx.serialization.json.Json

/**
 * Parser functions for TransactionEntity
 */
object TransactionParser {
    // Initialize JSON serializer with pretty printing
    private val json = Json { 
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    
    /**
     * Convert TransactionEntity to JSON string
     */
    fun toJson(transaction: TransactionEntity): String {
        return json.encodeToString(TransactionEntity.serializer(), transaction)
    }
    
    /**
     * Convert JSON string back to TransactionEntity
     */
    fun fromJson(jsonString: String): TransactionEntity {
        return json.decodeFromString(TransactionEntity.serializer(), jsonString)
    }
}
