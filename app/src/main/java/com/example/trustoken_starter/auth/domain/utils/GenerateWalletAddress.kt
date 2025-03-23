package com.example.trustoken_starter.auth.domain.utils

import java.security.SecureRandom
import kotlin.math.absoluteValue

fun generateSecureRandomNumber(): String {
    val secureRandom = SecureRandom()
    val number = secureRandom.nextLong().absoluteValue.toString().padStart(16, '0')
    return number.take(16)
}