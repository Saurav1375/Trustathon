package com.example.trustoken_starter.auth.domain.repository


import com.example.trustoken_starter.auth.domain.model.UserData
import com.example.trustoken_starter.payment.domain.model.Wallet
import kotlinx.coroutines.flow.Flow

interface UserWalletService {
    fun getCurrentUser(): Flow<UserData?>
    fun getCurrentUserWallet(): Flow<Wallet?>
    fun getUserWalletByAddress(walletAddress: String): Flow<Wallet?>
    fun getAllUsers(): Flow<List<UserData>>
    suspend fun saveWallet(wallet: Wallet): Boolean
}
