package com.example.trustoken_starter.payment.presentation.home_screen


import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.trustoken_starter.R
import com.example.trustoken_starter.core.navigation.Screen
import com.example.trustoken_starter.payment.presentation.components.AccountDialogue
import com.example.trustoken_starter.payment.presentation.components.BankActionsRow
import com.example.trustoken_starter.payment.presentation.components.EmptyStateMessage
import com.example.trustoken_starter.payment.presentation.components.PaymentCard
import com.example.trustoken_starter.payment.presentation.components.TransactionItem
import com.example.trustoken_starter.payment.presentation.utils.toDisplayableNumber
import com.example.trustoken_starter.ui.theme.BankColor
import com.example.trustoken_starter.ui.theme.BgColor

@Composable
fun HomeScreen(
    state: HomeState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onAction: (HomeAction) -> Unit,
    onSignOutAccount: () -> Unit,
) {
    var backPressedOnce by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val showDialog = remember {  mutableStateOf(false) }


    BackHandler {
        if (backPressedOnce) {
            (context as? Activity)?.finishAffinity() // Close the app and remove it from recent apps
        } else {
            backPressedOnce = true
            Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
    }

    AccountDialogue(
        userData = state.userData,
        onSignOutAccount = {
            onSignOutAccount() },
        showDialog = showDialog
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BankColor)
            .padding(top = 16.dp)
            .verticalScroll(rememberScrollState())

    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${state.wallet?.balance?.toDisplayableNumber()} BHC",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    modifier = Modifier.weight(2f),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Spacer(modifier = Modifier.width(15.dp))
                        AsyncImage(
                            modifier = Modifier
                                .border(1.dp, Color.White, CircleShape)
                                .size(50.dp)
                                .padding(3.dp)
                                .clip(CircleShape)
                                .clickable { showDialog.value = true },
                            model = state.userData?.profilePictureUrl,
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
            Text(
                text = "Available Balance",
                color = Color.White.copy(alpha = 0.5f),
                fontFamily = FontFamily(Font(R.font.poppins_light)),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            PaymentCard(state)
            Spacer(modifier = Modifier.height(20.dp))

        }
        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))
                    .background(BgColor)
                    .padding(20.dp),
            ) {
                BankActionsRow {
                    when (it) {
                        Screen.SendMoneyScreen.route -> onAction(HomeAction.OnSendClick)
                        Screen.PaymentRequestsScreen.route -> onAction(HomeAction.OnRequestClick)
                        else -> Unit
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(0.7f)
                    ) {
                        Text(
                            text = "Recent Transactions",
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            color = BankColor,
                            fontSize = 20.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }

                Spacer(modifier = Modifier.height(5.dp))

                val userWalletAddress = state.userData?.walletAddress ?: ""
                val items = state.allTransactions.filter {
                    it.toAddress == userWalletAddress || it.fromAddress == userWalletAddress
                }
                if (items.isEmpty()) {
                    EmptyStateMessage(
                        "No Recent Transactions",
                        Icons.Filled.Money
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                } else {
                    items.forEach { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            myAddress = userWalletAddress
                        ) {
                            onAction(HomeAction.OnTransactionClick(transaction))
                            onClick()
                        }

                    }
                }


            }

        }


    }
}














