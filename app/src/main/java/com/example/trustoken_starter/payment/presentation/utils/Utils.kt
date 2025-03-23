package com.example.trustoken_starter.payment.presentation.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Double.toDisplayableNumber(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return formatter.format(this)
}

fun formatDate(millis: Long): String {
    val dateFormat = SimpleDateFormat("MM/yy", Locale.getDefault())
    return dateFormat.format(Date(millis))
}