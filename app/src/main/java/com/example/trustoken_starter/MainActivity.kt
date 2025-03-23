package com.example.trustoken_starter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.trustoken_starter.auth.data.service.AuthUiClient
import com.example.trustoken_starter.auth.data.service.AuthWithUserPass
import com.example.trustoken_starter.core.navigation.NavigationGraph
import com.example.trustoken_starter.di.Injection
import com.example.trustoken_starter.ui.theme.TrusToken_StarterTheme
import com.google.android.gms.auth.api.identity.Identity

class MainActivity : ComponentActivity() {
    private val authUiClient by lazy {
        AuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext),
            Injection.instance()
        )
    }

    private val authWithUserPass by lazy{
        AuthWithUserPass(
            Injection.instance()
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrusToken_StarterTheme  {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavigationGraph(
                        navController = navController,
                        authUiClient = authUiClient,
                        authWithUserPass = authWithUserPass,
                        applicationContext = applicationContext,
                    )
                }
            }
        }
    }
}

