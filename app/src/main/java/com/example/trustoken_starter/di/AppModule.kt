package com.example.trustoken_starter.di

import com.example.trustoken_starter.auth.data.service.UserWalletServiceImpl
import com.example.trustoken_starter.auth.domain.repository.UserWalletService
import com.example.trustoken_starter.payment.presentation.home_screen.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { FirebaseFirestore.getInstance() }
    single { FirebaseAuth.getInstance() }
    singleOf(::UserWalletServiceImpl).bind<UserWalletService>()
    viewModelOf(::HomeViewModel)
}
