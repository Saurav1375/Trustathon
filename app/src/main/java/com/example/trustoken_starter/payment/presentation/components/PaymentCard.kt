package com.example.trustoken_starter.payment.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.trustoken_starter.R
import com.example.trustoken_starter.payment.presentation.home_screen.HomeState
import com.example.trustoken_starter.payment.presentation.utils.formatDate

enum class CardType(
    val title: String,
    @DrawableRes val image: Int
) {
    Visa("visa", R.drawable.ic_visa_logo)
}

@Composable
fun PaymentCard(
    state: HomeState
) {
    val visaType by remember { mutableStateOf(CardType.Visa) }
    val animatedColor = animateColorAsState(targetValue = Color(0xFF191970), label = "")

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(220.dp),
                elevation = CardDefaults.elevatedCardElevation(18.dp),
                colors = CardDefaults.cardColors(animatedColor.value),

                ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    // Top row with symbol and logo
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.card_symbol),
                            contentDescription = "Symbol"
                        )

                        Image(
                            painter = painterResource(id = visaType.image),
                            contentDescription = "Card Type"
                        )
                    }

                    // Card number in the center
                    state.wallet?.walletAddress?.chunked(4)?.let {
                        Text(
                            text = it.joinToString("  "),
                            style = MaterialTheme.typography.headlineSmall, // Material 3 typography
                            maxLines = 1,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            color = Color.White,
                            modifier = Modifier
                                .animateContentSize(spring())
                                .padding(vertical = 16.dp, horizontal = 16.dp)
                                .align(Alignment.Center)
                        )
                    }

                    // Bottom section with card holder and expiry info
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Card holder section
                        Column {
                            Text(
                                text = "CARD HOLDER",
                                style = MaterialTheme.typography.bodySmall, // Material 3 typography
                                color = Color.White.copy(alpha = 0.5f),
                                fontFamily = FontFamily(Font(R.font.poppins_medium))
                            )

                            Text(
                                text = state.userData?.username ?: "",
                                style = MaterialTheme.typography.bodyLarge, // Material 3 typography
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                modifier = Modifier.animateContentSize(TweenSpec(300))
                            )
                        }

                        // Expiry and CVV section
                        Row {
                            Column(
                                modifier = Modifier.padding(end = 16.dp)
                            ) {
                                Text(
                                    text = "CREATED AT",
                                    style = MaterialTheme.typography.bodySmall, // Material 3 typography
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontFamily = FontFamily(Font(R.font.poppins_medium))
                                )

                                Text(
                                    text = state.wallet?.creationDate?.let { formatDate(it) } ?: "",
                                    style = MaterialTheme.typography.bodyLarge, // Material 3 typography
                                    color = Color.White,
                                    fontFamily = FontFamily(Font(R.font.poppins_bold))
                                )
                            }

//                            Column {
//                                Text(
//                                    text = "CVV",
//                                    style = MaterialTheme.typography.bodySmall, // Material 3 typography
//                                    color = Color.White.copy(alpha = 0.5f),
//                                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
//                                    textAlign = TextAlign.End
//                                )
//
//                                Text(
//                                    text = "314",
//                                    style = MaterialTheme.typography.bodyLarge, // Material 3 typography
//                                    color = Color.White,
//                                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
//                                    textAlign = TextAlign.End
//                                )
//                            }
                        }
                    }
                }
            }
        }

    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun PaymentCardPreview() {
//    PaymentCard()
//
//}

















