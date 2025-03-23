package com.example.trustoken_starter.payment.presentation.home_screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.trustoken_starter.R
import com.example.trustoken_starter.core.navigation.Screen
import com.example.trustoken_starter.payment.presentation.components.BankActionsRow
import com.example.trustoken_starter.payment.presentation.components.CakeMarketplace
import com.example.trustoken_starter.payment.presentation.components.PaymentCard
import com.example.trustoken_starter.payment.presentation.utils.toDisplayableNumber
import com.example.trustoken_starter.ui.theme.BankColor
import com.example.trustoken_starter.ui.theme.BgColor

@Composable
fun HomeScreen(
    state: HomeState,
    modifier: Modifier = Modifier,
    onAction : (HomeAction) -> Unit
) {
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
                    text = "$ ${state.wallet?.balance?.toDisplayableNumber()}",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    modifier = Modifier.weight(1f),
                    fontSize = 30.sp,
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
                                .clip(CircleShape),
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))
                .background(BgColor)
                .padding(20.dp)
        ) {
            BankActionsRow {
                when (it) {
                    Screen.SendMoneyScreen.route-> onAction(HomeAction.OnSendClick)
                    else -> Unit
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            CakeMarketplace()

        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun HomeScreenPreview() {
//    HomeScreen()
//}














