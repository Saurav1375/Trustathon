package com.example.trustoken_starter.core.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.trustoken_starter.auth.data.service.AuthUiClient
import com.example.trustoken_starter.auth.data.service.AuthWithUserPass
import com.example.trustoken_starter.auth.presentation.AuthViewModel
import com.example.trustoken_starter.auth.presentation.forget_password.ForgetPassScreen
import com.example.trustoken_starter.auth.presentation.forget_password.ResetPassScreen
import com.example.trustoken_starter.auth.presentation.login.SignInScreen
import com.example.trustoken_starter.auth.presentation.register.SignUpScreen
import com.example.trustoken_starter.payment.presentation.home_screen.HomeAction
import com.example.trustoken_starter.payment.presentation.home_screen.HomeScreen
import com.example.trustoken_starter.payment.presentation.home_screen.HomeViewModel
import com.example.trustoken_starter.payment.presentation.payment_screen.PaymentScreen
import com.example.trustoken_starter.payment.presentation.user_screen.UserListScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun NavigationGraph(
    navController: NavHostController,
    authUiClient: AuthUiClient,
    authWithUserPass: AuthWithUserPass,
    applicationContext: Context,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current as Activity
    val viewModel: AuthViewModel = viewModel()
    val homeViewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val state1 = homeViewModel.state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()


    LaunchedEffect(key1 = Unit) {
        if (authUiClient.getSignedInUser() != null) {
            navController.navigate(Screen.HomeScreen.route)
        }
    }
    NavHost(
        navController = navController,
        startDestination = Screen.SignInScreen.route,
        modifier = modifier
    ) {

        composable(Screen.HomeScreen.route) {
            HomeScreen(
                state1.value,
                onAction = {
                    homeViewModel.onAction(it)
                    if (it is HomeAction.OnSendClick) {
                        navController.navigate(Screen.SendMoneyScreen.route)
                    }
                }
            )
        }

        composable(Screen.SendMoneyScreen.route) {
           UserListScreen(
                state1.value,
               onAction = {
                   homeViewModel.onAction(it)
                   if (it is HomeAction.OnUserClick) {
                       navController.navigate(Screen.PaymentScreen.route)
                   }
               }

            )
        }

        composable(Screen.PaymentScreen.route) {
            PaymentScreen(
                state = state1.value
            )
        }

        composable(Screen.SignInScreen.route) {
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        scope.launch {
                            val signInResult = authUiClient.SignInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)

                        }

                    }

                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Sign In Successful",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigate(Screen.HomeScreen.route)
                    viewModel.resetState()
                }

            }

            SignInScreen (
                navController,
                state = state,
                forgetPass = { navController.navigate(Screen.ForgetPassScreen.route) },
                onRegister = {
                    viewModel.resetState()
                    navController.navigate((Screen.SignUpScreen.route))
                },

                loginWithGoogle = {
                    scope.launch {
                        val signInIntentSender = authUiClient.SignIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )

                    }


                },
                loginWithFacebook = {

                },
            ) { email, pass ->
                scope.launch {
                    val result = authWithUserPass.signIn(email, pass)
                    viewModel.onSignInResult(result)
                }

            }
        }

        composable(Screen.SignUpScreen.route) {
            LaunchedEffect(key1 = state.isSignUpSuccessful) {
                if (state.isSignUpSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Sign Up Successful",
                        Toast.LENGTH_LONG
                    ).show()

                    scope.launch {
                        authUiClient.signOut()
                    }
                    navController.navigate(Screen.SignInScreen.route)
                    viewModel.resetState()


                }
            }
            SignUpScreen(
                state = state,
                onLogin = {
                    viewModel.resetState()
                    navController.navigate(Screen.SignInScreen.route) }
            ) { email, name, pass ->
                scope.launch {
                    val result = authWithUserPass.signUp(email, pass, name)
                    viewModel.onSignUpResult(result)
                }
            }

        }

        composable(Screen.ForgetPassScreen.route) {
            ForgetPassScreen {email->
                if (email.isNotEmpty()) {
                    authWithUserPass.sendPasswordResetEmail(email) { success ->
                        if (success) {
                            Toast.makeText(
                                context,
                                "Link to reset password has been sent to you email",
                                Toast.LENGTH_SHORT

                            ).show()
//                            navController.navigate(Screen.ResetPassScreen.route)
                        } else {
                            Toast.makeText(
                                context,
                                "Failed to send reset email.",
                                Toast.LENGTH_SHORT

                            ).show()

                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Please enter your email.",
                        Toast.LENGTH_SHORT

                    ).show()
                }

            }

        }
        composable(Screen.ResetPassScreen.route) {
            ResetPassScreen {
                navController.navigate(Screen.SignInScreen.route)
            }

        }

    }

}