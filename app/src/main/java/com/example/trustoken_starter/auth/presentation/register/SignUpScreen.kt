package com.example.trustoken_starter.auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trustoken_starter.R
import com.example.trustoken_starter.auth.presentation.SignInState
import com.example.trustoken_starter.auth.presentation.login.CustomTextField
import com.example.trustoken_starter.auth.presentation.login.displayLargeFontFamily


@Composable
fun SignUpScreen(
    state: SignInState,
    onLogin : () -> Unit,
    privacyPolicy : () -> Unit = {},
    onSignUp : (String, String, String) -> Unit,

    ) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError){
        state.signInError?.let { error->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Image(
                painter = painterResource(id = R.drawable.group__1_),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .aspectRatio(1.5f),
                contentScale = ContentScale.Fit // Fit the image within the width
            )
            Spacer(modifier = Modifier.height(8.dp))


            Text(
                modifier = Modifier,
                text = "Sign Up",
                color = Color.White,
                style = TextStyle(
                    fontFamily = displayLargeFontFamily,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            val email = remember { mutableStateOf("") }
            val name = remember { mutableStateOf("") }
            val password =  remember { mutableStateOf("") }
            CustomTextField(title = "Email ID", icon = Icons.Default.Email, value = email , onValueChange = {
                email.value = it
            })
            CustomTextField(title = "Full Name", icon = Icons.Default.AccountBox, value = name , onValueChange = {
                name.value = it
            })
            CustomTextField(title = "Password", icon = Icons.Default.Lock, value = password ,isPassField = true, onValueChange = {
                password.value = it
            })

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable { privacyPolicy() },
                    text = buildAnnotatedString {
                        append("By Signing up, you’re agree to our ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Color(23, 105, 227)
                            )

                        ){
                            append("Term & Conditions ")
                        }
                        append(" and ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Color(23, 105, 227)
                            )

                        ){
                            append("Privacy Policy")
                        }
                    },
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = displayLargeFontFamily,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Normal,

                        )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .background(Color(0, 50, 87), shape = RoundedCornerShape(10.dp))
                    .clickable {
                        onSignUp(
                        email.value,
                        name.value,
                        password.value
                    )
                               },

                ){
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "Continue",
                    color = Color.White,
                    style = TextStyle (
                        fontFamily = displayLargeFontFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal,

                        )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))



            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable { onLogin() },
                    text = buildAnnotatedString {
                        append("Joined before? ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold
                            )

                        ){
                            append("Login")
                        }
                    },
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = displayLargeFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,

                        )
                )
            }

        }
    }
}


