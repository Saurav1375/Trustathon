package com.example.trustoken_starter.core.navigation

/**
 * A sealed interface representing the routes of screens in the application..
 */
sealed class Screen(
    val route: String
) {

    data object HomeScreen : Screen("home_screen")
    data object SignInScreen : Screen("signin_screen")
    data object SignUpScreen : Screen("signup_screen")
    data object ForgetPassScreen : Screen("forgetpass_screen")
    data object ResetPassScreen : Screen("resetpass_screen")
    data object SendMoneyScreen : Screen("sendmoney_screen")
    data object PaymentScreen : Screen("payment_screen")
    data object PasswordScreen : Screen("password_screen")
    data object TransactionDetails : Screen("transaction_details")
    data object PaymentRequestsScreen : Screen("payment_requests")
    data object ConfirmPaymentRequestScreen : Screen("confirm_payment_request")
}