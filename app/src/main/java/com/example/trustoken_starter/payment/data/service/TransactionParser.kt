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

// Example usage:
fun main() {
    // Create a sample transaction
    val transaction = TransactionEntity(
        id = "tx123",
        fromAddress = "0x123abc",
        toAddress = "0x456def",
        amount = 1.5,
        reason = "Payment for services",
        timestamp = System.currentTimeMillis(),
        status = "COMPLETED",
        transactionString = "Raw transaction data",
        signature = "0xsignature789"
    )
    
    // Convert to JSON
    val jsonString = TransactionParser.toJson(transaction)
    println("JSON representation:")
    println(jsonString)
    
    // Convert back to object
    val parsedTransaction = TransactionParser.fromJson(jsonString)
    println("\nParsed back to object:")
    println(parsedTransaction)
    
    // Verify equality
    println("\nOriginal equals parsed: ${transaction == parsedTransaction}")
}