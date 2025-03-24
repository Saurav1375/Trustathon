package com.example.trustoken_starter.auth.domain.utils

import java.security.SecureRandom
import kotlin.math.absoluteValue

/**
 * Generates a cryptographically secure random number.
 *
 * @return A 16-digit random number as a String.
 *         The number is always positive and zero-padded to ensure it is exactly 16 digits.
 */
fun generateSecureRandomNumber(): String {
    val secureRandom = SecureRandom() // Initialize a cryptographically strong random number generator.
    val number = secureRandom.nextLong().absoluteValue.toString().padStart(16, '0') // Ensure positive value and pad to 16 digits.
    return number.take(16) // Return only the first 16 digits.
}