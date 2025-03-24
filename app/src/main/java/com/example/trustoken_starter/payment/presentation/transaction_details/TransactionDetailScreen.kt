package com.example.trustoken_starter.payment.presentation.transaction_details

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trustoken_starter.payment.presentation.home_screen.HomeState
import com.example.trustoken_starter.payment.presentation.utils.formatLongToDateTime
import com.example.trustoken_starter.ui.theme.TextColor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Color theme based on your image
object AppColors {
    val PrimaryBlue = Color(0xFF1E5EB3)
    val DarkBlue = Color(0xFF0A215A)
    val LightBlue = Color(0xFF5B8DD6)
    val AccentCoral = Color(0xFFFF7A7A)
    val BackgroundGrey = Color(0xFFF5F5F5)
    val CardBlue = Color(0xFF1A3A7A)
    val GoldYellow = Color(0xFFE6C350)
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(state: HomeState, onBackPressed: () -> Unit) {

    BackHandler {
        onBackPressed()
    }
    val transaction = state.transaction

    if (transaction != null) {
        val isIncoming = transaction.toAddress == state.userData?.walletAddress
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.BackgroundGrey)
        ) {
            // Top App Bar
            SmallTopAppBar(
                title = { Text("Transaction Details") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = AppColors.PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )

            // Transaction Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Status Icon
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                when (transaction.status) {
                                    "Completed" -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                                    "Pending" -> Color(0xFFFFC107).copy(alpha = 0.2f)
                                    "Failed" -> Color(0xFFE91E63).copy(alpha = 0.2f)
                                    else -> Color(0xFF9E9E9E).copy(alpha = 0.2f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (transaction.status) {
                                "Completed" -> Icons.Default.CheckCircle
                                "Pending" -> Icons.Default.Schedule
                                "Failed" -> Icons.Default.Cancel
                                else -> Icons.Default.Help
                            },
                            contentDescription = transaction.status,
                            tint = when (transaction.status) {
                                "Completed" -> Color(0xFF4CAF50)
                                "Pending" -> Color(0xFFFFC107)
                                "Failed" -> Color(0xFFE91E63)
                                else -> Color(0xFF9E9E9E)
                            },
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Amount
                    Text(
                        text = if (isIncoming) "+${String.format("%,.2f", transaction.amount)} BHC"
                        else "-${String.format("%,.2f", transaction.amount)} BHC",
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = if (isIncoming) Color(0xFF4CAF50) else Color(0xFFE91E63)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Status
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                when (transaction.status) {
                                    "Completed" -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                                    "Pending" -> Color(0xFFFFC107).copy(alpha = 0.2f)
                                    "Failed" -> Color(0xFFE91E63).copy(alpha = 0.2f)
                                    else -> Color(0xFF9E9E9E).copy(alpha = 0.2f)
                                }
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = transaction.status,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = when (transaction.status) {
                                "Completed" -> Color(0xFF4CAF50)
                                "Pending" -> Color(0xFFFFC107)
                                "Failed" -> Color(0xFFE91E63)
                                else -> Color(0xFF9E9E9E)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Divider
                    Divider()

                    Spacer(modifier = Modifier.height(24.dp))

                    // Transaction Details
                    DetailRow(
                        label = "Transaction ID",
                        value = transaction.id,
                        icon = Icons.Default.LabelImportant
                    )

                    DetailRow(
                        label = "Reason",
                        value = transaction.reason,
                        icon = Icons.Default.Description
                    )

                    DetailRow(
                        label = if (isIncoming) "From" else "To",
                        value = if (isIncoming) transaction.fromAddress else transaction.toAddress,
                        icon = Icons.Default.AccountBalance
                    )

                    DetailRow(
                        label = "Date & Time",
                        value = formatLongToDateTime(state.transaction.timestamp),
                        icon = Icons.Default.Schedule
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Action Button
                    Button(
                        onClick = { /* Handle action */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.PrimaryBlue
                        )
                    ) {
                        Text(
                            text = if (transaction.status == "PENDING") "Track Status" else "Download Receipt",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextColor
                        )
                    }
                }
            }
        }
    }
    

}

@Composable
fun DetailRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppColors.PrimaryBlue,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = AppColors.DarkBlue
            )
        }
    }
}


fun shortenAddress(address: String): String {
    return if (address.length > 10) {
        "${address.take(6)}...${address.takeLast(4)}"
    } else {
        address
    }
}

@SuppressLint("NewApi")
fun formatDate(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy • HH:mm")
    return dateTime.format(formatter)
}

fun formatDetailDate(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss")
    return dateTime.format(formatter)
}
