package com.example.trustoken_starter.payment.domain.model

data class Wallet(
    val userId: String = "",
    val publicKey: String = "",
    val walletAddress: String = "",
    var balance: Double = 0.0,
    val creationDate: Long = System.currentTimeMillis()
)