package com.example.trustoken_starter.payment.presentation.payment_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trustoken_starter.payment.presentation.home_screen.HomeAction
import com.example.trustoken_starter.payment.presentation.home_screen.HomeState
import com.example.trustoken_starter.payment.presentation.user_screen.components.UserAvatar

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val amountFocusRequester = remember { FocusRequester() }
    val reasonFocusRequester = remember { FocusRequester() }
    val amount = remember { mutableStateOf("") }
    val reason = remember { mutableStateOf("") }

    // State for showing reason input
    val (isReasonInputVisible, setReasonInputVisible) = remember { mutableStateOf(false) }
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Payment users section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Sender avatar
                UserAvatar(
                    profilePictureUrl = state.userData?.profilePictureUrl ?: "",
                    username = state.userData?.username ?: ""
                )

                // Arrow
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "to",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                // Receiver avatar
                UserAvatar(
                    profilePictureUrl = state.selectedUser?.profilePictureUrl ?: "",
                    username = state.selectedUser?.username ?: ""
                )
            }

            Text(
                text = "Paying ${state.selectedUser?.username}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))


            // Use BasicTextField for a more customizable text field
            BasicTextField(
                value = amount.value,
                onValueChange = { newValue ->
                    // Only allow valid currency input
                    if (newValue.length <= 10) {
                        amount.value = newValue
                    }
                },
                textStyle = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier
                    .focusRequester(amountFocusRequester)
                    // Make it tappable to focus
                    .clickable { amountFocusRequester.requestFocus() }
                    .padding(vertical = 8.dp),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Row {

                                innerTextField()
                                Text(
                                    text = "BHC",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 32.sp

                                )

                            }
                        }
                    }
                }
            )

            LaunchedEffect(Unit) {
                // Prevent system keyboard from showing up
                focusManager.clearFocus()
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .focusRequester(reasonFocusRequester)
                    .height(48.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable {
                        setReasonInputVisible(true)
                        reasonFocusRequester.requestFocus()
                    }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!isReasonInputVisible && reason.value.isEmpty()) {
                    // Show as button when no input
                    Text(
                        text = "What is this for?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    // Show as text field when active or has content
                    BasicTextField(
                        value = reason.value,
                        onValueChange = {
                            reason.value = it
                        },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onPrimaryContainer),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(reasonFocusRequester),
                        decorationBox = { innerTextField ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (reason.value.isEmpty()) {
                                    Text(
                                        text = "Add Note",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                            alpha = 0.6f
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(128.dp))

            // Continue button
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable {
                        if (amount.value.isEmpty() || amount.value
                                .filter { it.isDigit() }
                                .toDouble() <= 0.0
                        ) {
                            Toast
                                .makeText(
                                    context,
                                    "Please enter a valid amount",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        } else {
                            onAction(
                                HomeAction.Confirm(
                                    amount = amount.value
                                        .toDouble(),
                                    reason = reason.value
                                )
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Continue",
                    tint = MaterialTheme.colorScheme.primary
                )
            }


        }
    }
}
