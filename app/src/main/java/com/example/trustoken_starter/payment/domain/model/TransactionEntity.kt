package com.example.trustoken_starter.payment.domain.model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@SuppressLint("NewApi")
@Serializable
data class TransactionEntity(
    val id: String = "",
    val fromAddress: String = "",
    val toAddress: String = "",
    val amount: Double = 0.0,
    val reason : String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "",
    val transactionString: String = "",
    val signature: String = "",
)
