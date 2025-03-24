package com.example.trustoken_starter.payment.presentation.components
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.trustoken_starter.R
import com.example.trustoken_starter.auth.domain.model.UserData


@Composable
fun AccountDialogue(
    userData: UserData?,
    onSignOutAccount: () -> Unit,
    showDialog: MutableState<Boolean>
) {
//    var showDialog by mutableStateOf(false)

    if (showDialog.value) {
        Dialog(
            onDismissRequest = { showDialog.value = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .size(340.dp, 320.dp)
                    .clip(RoundedCornerShape(60.dp))
                    .background(Color(40, 42, 44)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(85.dp)
                            .background(color = Color.Unspecified, shape = CircleShape)
                            .clipToBounds()

                    ) {
                        if (userData?.profilePictureUrl != null) {
                            AsyncImage(
                                model = userData.profilePictureUrl,
                                contentDescription = "profile picture",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop

                            )

                        } else {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(id = R.drawable.logo2),
                                contentDescription = "profile picture",
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxWidth(0.75f), contentAlignment = Alignment.Center){
                        if (userData != null) {
                            userData.username?.let {
                                Text(
                                    text = it,
                                    style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center
                                    ),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(4.dp))

                    if (userData != null) {
                        Text(
                            text = userData.emailId,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                color = Color(135, 135, 135),
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(50.dp)
                            .background(Color.Transparent, shape = RoundedCornerShape(10.dp))
                            .border(
                                BorderStroke(1.dp, Color.White),
                                shape = RoundedCornerShape(10.dp)
                            )

                            .clickable {
                                onSignOutAccount()

                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sign Out",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 17.sp
                            )
                        )

                    }


                }
            }

        }

    }


}

