package com.example.trustoken_starter.payment.domain

data class Wallet(
    val userId: String = "",
    val publicKey: String = "",
    val walletAddress: String = "",
    val balance: Double = 0.0,
    val creationDate: Long = System.currentTimeMillis()
)