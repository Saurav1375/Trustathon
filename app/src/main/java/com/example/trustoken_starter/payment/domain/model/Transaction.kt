package com.example.trustoken_starter.payment.domain.model

import java.time.LocalDateTime

data class Transaction(
    val id: String,
    val fromAddress: String,
    val toAddress: String,
    val amount: Double = 0.0,
    val reason : String,
    val timestamp: LocalDateTime,
    val status: String
)

enum class TransactionStatus {
    PENDING,
    CONFIRMED,
    FAILED
}