package com.example.trustoken_starter.payment.presentation.user_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.trustoken_starter.payment.presentation.home_screen.HomeAction
import com.example.trustoken_starter.payment.presentation.home_screen.HomeState
import com.example.trustoken_starter.payment.presentation.transaction_details.AppColors
import com.example.trustoken_starter.payment.presentation.user_screen.components.UserCard
import com.example.trustoken_starter.ui.theme.BgColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    state: HomeState,
    modifier: Modifier = Modifier,
    onAction: (HomeAction) -> Unit = {}
) {

    Surface(
        modifier = modifier.fillMaxSize(),
        color = BgColor
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SmallTopAppBar(
                title = { Text("Users") },
                navigationIcon = {
//                IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back"
//                    )
//                }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = AppColors.PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )

            if (state.isLoading) {
                Box (
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }
             else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(state.allUsers.filter { it.userId != state.userData?.userId }) { user ->
                        UserCard(user = user) {
                            onAction(HomeAction.OnUserClick(user))
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}


