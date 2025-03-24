package com.example.trustoken_starter.payment.presentation.payment_request
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.trustoken_starter.payment.presentation.components.EmptyStateMessage
import com.example.trustoken_starter.payment.presentation.components.TransactionItem
import com.example.trustoken_starter.payment.presentation.home_screen.HomeAction
import com.example.trustoken_starter.payment.presentation.home_screen.HomeState
import com.example.trustoken_starter.payment.presentation.transaction_details.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentRequestListScreen(
    state : HomeState,
    onAction: (HomeAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.BackgroundGrey)
    ) {

        // Transaction List Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        ) {
            SmallTopAppBar(
                title = { Text("Payment Requests") },
                navigationIcon = {
//                IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back"
//                    )
//                }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = AppColors.PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            val transaction = state.allTransactions.filter {
                it.status == "PENDING" && it.toAddress == state.userData?.walletAddress
            }
            if (transaction.isEmpty()) {
                EmptyStateMessage(
                    message = "No Transactions Requests Right Now",
                    icon = Icons.Default.Payment,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {

                    items(transaction) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            onClick = {
                                onAction(HomeAction.OnTransactionClick(transaction))
                            },
                            myAddress = state.userData?.walletAddress ?: ""
                        )
                        HorizontalDivider()
                    }
                }
            }
            

        }
    }
}