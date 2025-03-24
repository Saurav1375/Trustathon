package com.example.trustoken_starter.payment.presentation.pin_verification


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trustoken_starter.payment.presentation.pin_verification.components.OtpInputField

@Composable
fun ActivationScreen(
    state: ActivationState,
    focusRequesters: List<FocusRequester>,
    onAction: (ActivationActions) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "TrusToken Detected",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp
        )

        Text(
            "Your secure token has been detected. Please continue to authenticate the transaction.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp
        )


        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            state.code.forEachIndexed { index, number ->
                OtpInputField(
                    number = number,
                    focusRequester = focusRequesters[index],
                    onFocusChanged = { isFocused ->
                        if(isFocused) {
                            onAction(ActivationActions.OnChangeFieldFocused(index))
                        }
                    },
                    onNumberChanged = { newNumber ->
                        onAction(ActivationActions.OnEnterNumber(newNumber, index))
                    },
                    onKeyboardBack = {
                        onAction(ActivationActions.OnKeyboardBack)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        }

    }
}