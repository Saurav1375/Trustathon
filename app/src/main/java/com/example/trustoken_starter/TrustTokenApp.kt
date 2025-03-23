package com.example.trustoken_starter

import android.app.Application
import com.example.trustoken_starter.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TrustTokenApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TrustTokenApp)
            androidLogger()
            modules(appModule)
        }
    }
}