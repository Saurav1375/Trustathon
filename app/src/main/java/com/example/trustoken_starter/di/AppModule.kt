package com.example.trustoken_starter.di

import com.example.trustoken_starter.auth.data.service.UserWalletServiceImpl
import com.example.trustoken_starter.auth.domain.repository.UserWalletService
import com.example.trustoken_starter.payment.data.service.TransactionServiceImpl
import com.example.trustoken_starter.payment.domain.service.TransactionService
import com.example.trustoken_starter.payment.presentation.home_screen.HomeViewModel
import com.example.trustoken_starter.payment.presentation.pin_verification.ActivationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { FirebaseFirestore.getInstance() }
    single { FirebaseAuth.getInstance() }
    single {
        FirebaseDatabase.getInstance("https://trusttoken-b8297-default-rtdb.asia-southeast1.firebasedatabase.app").apply {
            setPersistenceEnabled(true)
            setPersistenceCacheSizeBytes(10 * 1024 * 1024)
        }
    }
    singleOf(::UserWalletServiceImpl).bind<UserWalletService>()
    singleOf(::TransactionServiceImpl).bind<TransactionService>()
    viewModelOf(::HomeViewModel)
    viewModelOf(::ActivationViewModel)
}
