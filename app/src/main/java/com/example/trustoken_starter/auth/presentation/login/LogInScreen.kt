package com.example.trustoken_starter.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.trustoken_starter.R
import com.example.trustoken_starter.auth.presentation.SignInState

@OptIn(ExperimentalTextApi::class)
val displayLargeFontFamily = FontFamily(
    Font(
        R.font.plusjakartasans_variablefont_wght,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(430),
            FontVariation.width(90f),
            FontVariation.slant(-6f),
        )
    )
)

@Composable
fun SignInScreen(
    navController: NavHostController,
    state: SignInState,
    forgetPass: () -> Unit,
    onRegister: () -> Unit,
    loginWithGoogle: () -> Unit = {},
    loginWithFacebook: () -> Unit = {},
    onSignIn: (String, String) -> Unit,
) {

//    BackHandler {
//        navController.navigate(Screen.OnBoarding2.route)
//    }
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
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
                .padding(top = 30.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Image(
                painter = painterResource(id = R.drawable.group),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .aspectRatio(1.3f),
                contentScale = ContentScale.Fit // Fit the image within the width
            )


            Text(
                modifier = Modifier,
                text = "Login",
                color = Color.White,
                style = TextStyle(
                    fontFamily = displayLargeFontFamily,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            val email = remember { mutableStateOf("") }
            val password = remember { mutableStateOf("") }
            CustomTextField(
                title = "Email ID",
                icon = Icons.Default.Email,
                value = email,
                onValueChange = {
                    email.value = it
                })
            CustomTextField(
                title = "Password",
                icon = Icons.Default.Lock,
                value = password,
                isPassField = true,
                onValueChange = {
                    password.value = it
                })

            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.End)
                    .clickable {
                        forgetPass()
                    },
                text = "Forget password?",
                color = Color.White,
                style = TextStyle(
                    fontFamily = displayLargeFontFamily,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Normal
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.16f)
                    .background(Color(0, 50, 87), shape = RoundedCornerShape(10.dp))
                    .clickable {
                        onSignIn(
                            email.value,
                            password.value
                        )
                        println(email.value)
                        println(password.value)
                    },

                ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "Continue",
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = displayLargeFontFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal,

                        )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.15f)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.24f)
                    .background(Color.White, shape = RoundedCornerShape(10.dp))
                    .clickable {
                        loginWithGoogle()
                    },

                ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Icon(
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .size(25.dp),
                        painter = painterResource(id = R.drawable._icon__google_),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center),
                            text = "Login with Google",
                            color = Color.Black,
                            style = TextStyle(
                                fontFamily = displayLargeFontFamily,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,

                                )
                        )

                    }
                }

            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 8.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable { onRegister() },
                    text = buildAnnotatedString {
                        append("New to us? ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold
                            )

                        ) {
                            append("Register")
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


@Composable
fun CustomTextField(
    title: String,

    icon: ImageVector, // Resource ID for the icon
    value: MutableState<String>, // Mutable state to store the text input value
    isPassField: Boolean = false,
    onValueChange: (String) -> Unit // Lambda to handle the text input changes
) {
    var showTitle by remember { mutableStateOf(true) }
    var isFocused by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp),
                tint = Color.White
            )

            // BasicTextField with the value and onValueChange
            Box(
                modifier = Modifier.fillMaxWidth(0.9f)

            ) {

                if (showTitle) {
                    Text(
                        modifier = Modifier.padding(top = 6.dp, start = 4.dp),
                        text = title,
                        style = TextStyle(
                            fontFamily = displayLargeFontFamily,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )

                    )
                }

                BasicTextField(
                    value = value.value,
                    onValueChange = {
                        onValueChange(it)
                    },
                    textStyle = TextStyle(
                        fontFamily = displayLargeFontFamily,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                            if (isFocused || value.value.isNotEmpty()) {
                                showTitle = false

                            } else {
                                showTitle = true
                                println("Text field unfocused")
                            }
                        },
                    singleLine = true,
                    visualTransformation = if (isPassField) {
                        if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                    } else VisualTransformation.None,
                )

            }

            val icon =
                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff

            if (isPassField && value.value.isNotEmpty()) {
                IconButton(
                    modifier = Modifier,
                    onClick = { passwordVisible = !passwordVisible }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = Color.White
                    )
                }

            }

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, top = 4.dp)
                .height(1.dp)
                .background(Color.LightGray)
        )
    }
}

