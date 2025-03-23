package com.example.trustoken_starter.payment.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trustoken_starter.R
import com.example.trustoken_starter.core.navigation.Screen
import com.example.trustoken_starter.ui.theme.BankColor

@Composable
fun BankActionsRow(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {}
) {
    val actions = listOf(
        BankAction(R.drawable.send, "Send", Screen.SendMoneyScreen.route),
        BankAction(R.drawable.request, "Request"),
        BankAction(R.drawable.loan, "Loan"),
        BankAction(R.drawable.top_up, "Topup")
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        actions.forEach { action ->
            BankActionItem(action) {
                onClick(it)
            }
        }
    }
}

@Composable
fun BankActionItem(action: BankAction, onClick: (String) -> Unit = {}) {
    Column(
        modifier = Modifier.clickable {
            if (action.route.isNotEmpty()) {
                onClick(action.route)
            }
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, BankColor, RoundedCornerShape(20.dp))
                .background(BankColor)
                .size(65.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = action.iconRes),
                contentDescription = action.label,
                modifier = Modifier.size(35.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = action.label,
            fontWeight = FontWeight.SemiBold,
            color = BankColor.copy(alpha = 0.7f),
            fontFamily = FontFamily(Font(R.font.poppins_light)),
            fontSize = 14.sp
        )
    }
}

data class BankAction(
    @DrawableRes val iconRes: Int,
    val label: String,
    val route: String = ""
)
