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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.example.trustoken_starter.core.presentation.util.ObserveAsEvents
import com.example.trustoken_starter.payment.presentation.home_screen.HomeAction
import com.example.trustoken_starter.payment.presentation.home_screen.HomeEvent
import com.example.trustoken_starter.payment.presentation.home_screen.HomeScreen
import com.example.trustoken_starter.payment.presentation.home_screen.HomeViewModel
import com.example.trustoken_starter.payment.presentation.payment_request.ConfirmPaymentScreen
import com.example.trustoken_starter.payment.presentation.payment_request.PaymentRequestListScreen
import com.example.trustoken_starter.payment.presentation.payment_screen.PaymentScreen
import com.example.trustoken_starter.payment.presentation.pin_verification.ActivationActions
import com.example.trustoken_starter.payment.presentation.pin_verification.ActivationEvents
import com.example.trustoken_starter.payment.presentation.pin_verification.ActivationScreen
import com.example.trustoken_starter.payment.presentation.pin_verification.ActivationViewModel
import com.example.trustoken_starter.payment.presentation.transaction_details.TransactionDetailScreen
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

    ObserveAsEvents(events = homeViewModel.events) { event ->
        when(event) {
            is HomeEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error,
                    Toast.LENGTH_LONG
                ).show()

                navController.navigate(Screen.PaymentScreen.route)
            }

            is HomeEvent.NavigateToPayment ->  {
                Toast.makeText(
                    context,
                    "SUCCESS",
                    Toast.LENGTH_LONG
                ).show()

                navController.navigate(Screen.PasswordScreen.route)
            }
            HomeEvent.Success -> TODO()
            is HomeEvent.PaymentError -> {
                Toast.makeText(
                    context,
                    event.error,
                    Toast.LENGTH_LONG
                ).show()
                navController.navigate(Screen.PaymentScreen.route)
            }

            HomeEvent.PaymentSuccessful -> {
                Toast.makeText(
                    context,
                    "Payment/Request Successful",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
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
                    if (it is HomeAction.OnRequestClick) {
                        navController.navigate(Screen.PaymentRequestsScreen.route)
                    }
                },
                onClick = {
                    navController.navigate(Screen.TransactionDetails.route)
                },

                onSignOutAccount = {
                    scope.launch {
                        authUiClient.signOut()
                        Toast.makeText(
                            applicationContext,
                            "Signed Out",
                            Toast.LENGTH_LONG
                        ).show()
                        navController.navigate(Screen.SignInScreen.route)
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
                state = state1.value,
                onAction = {
                    homeViewModel.onAction(it)
                }
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

        composable(route = Screen.PasswordScreen.route) {
            val activationViewModel = koinViewModel<ActivationViewModel>()
            val state by activationViewModel.state.collectAsStateWithLifecycle()

            val context = LocalContext.current
            ObserveAsEvents(events = activationViewModel.events) { event ->
                when(event) {
                    is ActivationEvents.Error -> {
                        Toast.makeText(
                            context,
                            "Incorrect Password Please remove and reconnect the Token again",
                            Toast.LENGTH_LONG
                        ).show()
                        navController.navigateUp()
                    }

                    ActivationEvents.Success -> {
//                        Toast.makeText(
//                            context,
//                            "Payment Successful Waiting for the recipient to confirm  ",
//                            Toast.LENGTH_LONG
//                        ).show()
                        homeViewModel.onAction(HomeAction.Transaction)
                        navController.navigate(Screen.TransactionDetails.route)
                    }
                }
            }

            val focusRequesters = remember {
                List(6) { FocusRequester() }
            }
            val focusManager = LocalFocusManager.current
            val keyboardManager = LocalSoftwareKeyboardController.current

            LaunchedEffect(state.focusedIndex) {
                state.focusedIndex?.let { index ->
                    focusRequesters.getOrNull(index)?.requestFocus()
                }
            }

            LaunchedEffect(state.code, keyboardManager) {
                val allNumbersEntered = state.code.none { it == null }
                if(allNumbersEntered) {
                    focusRequesters.forEach {
                        it.freeFocus()
                    }
                    focusManager.clearFocus()
                    keyboardManager?.hide()
                }
            }

            ActivationScreen(
                state = state,
                focusRequesters = focusRequesters,
                onAction = { action ->
                    when(action) {
                        is ActivationActions.OnEnterNumber -> {
                            if(action.number != null) {
                                focusRequesters[action.index].freeFocus()
                            }
                        }
                        else -> Unit
                    }
                    activationViewModel.onAction(action)
                },
                modifier = modifier
            )
        }

        composable(Screen.TransactionDetails.route) {
            TransactionDetailScreen(
                state = state1.value
            ) {
                navController.navigate(Screen.HomeScreen.route)
            }

        }

        composable(Screen.PaymentRequestsScreen.route) {
            PaymentRequestListScreen(
                state = state1.value,
                onAction = {
                    homeViewModel.onAction(it)
                    navController.navigate(Screen.ConfirmPaymentRequestScreen.route)
                }
            )
        }

        composable(Screen.ConfirmPaymentRequestScreen.route) {
            ConfirmPaymentScreen(
                state = state1.value,
                onAction = {
                    homeViewModel.onAction(it)
                }
            )
        }

    }

}