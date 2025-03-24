package com.example.trustoken_starter.payment.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trustoken_starter.payment.domain.model.TransactionEntity
import com.example.trustoken_starter.payment.presentation.transaction_details.AppColors
import com.example.trustoken_starter.payment.presentation.transaction_details.shortenAddress
import com.example.trustoken_starter.payment.presentation.utils.formatDate

@SuppressLint("DefaultLocale")
@Composable
fun TransactionItem(modifier: Modifier = Modifier, transaction: TransactionEntity,myAddress: String,  onClick: () -> Unit) {
    val isIncoming = transaction.toAddress == myAddress
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Transaction icon
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(if (isIncoming) AppColors.LightBlue else AppColors.AccentCoral),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isIncoming) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                    contentDescription = if (isIncoming) "Received" else "Sent",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transaction.reason.ifEmpty { "No Subject" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AppColors.DarkBlue,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = if (isIncoming) "From: ${shortenAddress(transaction.fromAddress)}" 
                           else "To: ${shortenAddress(transaction.toAddress)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = formatDate(transaction.timestamp),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = if (isIncoming) "+${String.format("%,.2f", transaction.amount)} BHC"
                           else "-${String.format("%,.2f", transaction.amount)} BHC",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (isIncoming) Color(0xFF4CAF50) else Color(0xFFE91E63)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
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
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = transaction.status,
                        fontSize = 12.sp,
                        color = when (transaction.status) {
                            "Completed" -> Color(0xFF4CAF50)
                            "Pending" -> Color(0xFFFFC107)
                            "Failed" -> Color(0xFFE91E63)
                            else -> Color(0xFF9E9E9E)
                        }
                    )
                }
            }
        }
    }
}