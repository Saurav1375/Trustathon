package com.example.trustoken_starter.payment.presentation.user_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.trustoken_starter.auth.domain.model.UserData
import com.example.trustoken_starter.ui.theme.BankColor
import com.example.trustoken_starter.ui.theme.GreenColor
import com.example.trustoken_starter.ui.theme.TextColor

@Composable
fun UserCard(
    user: UserData,
    modifier: Modifier = Modifier,
    onClick: (UserData) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable {
                onClick(user)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = BankColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(
                profilePictureUrl = user.profilePictureUrl, 
                username = user.username ?: user.userId
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.username ?: "No username",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = user.emailId,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextColor
                )
            }
            
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "View Details",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
