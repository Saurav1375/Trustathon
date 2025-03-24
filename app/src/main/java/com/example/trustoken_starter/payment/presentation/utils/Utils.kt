package com.example.trustoken_starter.payment.presentation.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Formats a `Double` value to a displayable number with two decimal places.
 * Example:
 * ```
 * val amount = 1234.5
 * val formatted = amount.toDisplayableNumber() // "1,234.50"
 * ```
 * @return A string representation of the number formatted based on the device's locale.
 */
fun Double.toDisplayableNumber(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return formatter.format(this)
}

/**
 * Converts a timestamp (milliseconds since epoch) into a formatted date string.
 * The format used is `"MM/yy"`, which represents the month and year.
 * Example:
 * ```
 * val formattedDate = formatDate(1672531200000) // "01/23"
 * ```
 * @param millis The timestamp in milliseconds.
 * @return A formatted date string in "MM/yy" format.
 */
fun formatDate(millis: Long): String {
    val dateFormat = SimpleDateFormat("MM/yy", Locale.getDefault())
    return dateFormat.format(Date(millis))
}

/**
 * Converts an epoch timestamp (milliseconds) into a human-readable date-time string.
 * The format used is `"MMM dd yyyy HH:mm:ss"`, which includes:
 * - Month (short form)
 * - Day
 * - Year
 * - Hours, minutes, and seconds
 * Example:
 * ```
 * val formattedDateTime = formatLongToDateTime(1672531200000) // "Jan 01 2023 00:00:00"
 * ```
 * @param epochMillis The timestamp in milliseconds.
 * @return A formatted date-time string in `"MMM dd yyyy HH:mm:ss"` format.
 */
fun formatLongToDateTime(epochMillis: Long): String {
    val sdf = SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.ENGLISH)
    return sdf.format(Date(epochMillis))
}
