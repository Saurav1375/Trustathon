package com.example.trustoken_starter.auth.domain.repository


import com.example.trustoken_starter.auth.domain.model.UserData
import com.example.trustoken_starter.payment.domain.Wallet
import kotlinx.coroutines.flow.Flow

interface UserWalletService {
    suspend fun getCurrentUser(): UserData?
    suspend fun getCurrentUserWallet(): Wallet?
    suspend fun getAllUsers(): Flow<List<UserData>>
}
