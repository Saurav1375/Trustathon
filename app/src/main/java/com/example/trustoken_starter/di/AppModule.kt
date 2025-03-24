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


/**
 * Dependency Injection module using Koin for managing app-wide dependencies.
 */
val appModule = module {

    /**
     * Provides a singleton instance of Firebase Firestore for cloud database operations.
     */
    single { FirebaseFirestore.getInstance() }

    /**
     * Provides a singleton instance of Firebase Authentication for user authentication.
     */
    single { FirebaseAuth.getInstance() }

    /**
     * Provides a singleton instance of Firebase Realtime Database.
     * - Uses a specific database URL for regional performance optimization.
     * - Enables offline persistence to cache data locally.
     * - Sets a cache size limit of 10MB.
     */
    single {
        FirebaseDatabase.getInstance("https://trusttoken-b8297-default-rtdb.asia-southeast1.firebasedatabase.app")
            .apply {
                setPersistenceEnabled(true)
                setPersistenceCacheSizeBytes(10 * 1024 * 1024) // 10MB cache
            }
    }

    /**
     * Provides a singleton instance of `UserWalletServiceImpl` and binds it to `UserWalletService` interface.
     */
    singleOf(::UserWalletServiceImpl).bind<UserWalletService>()

    /**
     * Provides a singleton instance of `TransactionServiceImpl` and binds it to `TransactionService` interface.
     */
    singleOf(::TransactionServiceImpl).bind<TransactionService>()

    /**
     * Provides a ViewModel instance of `HomeViewModel` using Koin's ViewModel provider.
     */
    viewModelOf(::HomeViewModel)

    /**
     * Provides a ViewModel instance of `ActivationViewModel` for PIN verification.
     */
    viewModelOf(::ActivationViewModel)
}
