package com.example.trustoken_starter.auth.domain.model

data class UserData(
    val userId: String = "",
    val username : String? = "",
    val profilePictureUrl : String? = "",
    val emailId: String = "",
    val walletAddress: String = "",
)