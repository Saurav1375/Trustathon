package com.example.trustoken_starter.payment.data.service

import android.annotation.SuppressLint
import android.content.Context
import com.example.trustoken_starter.TrusToken

/**
 * Service responsible for handling token-related operations such as authentication,
 * transaction signing, and encryption/decryption.
 */
object TokenService {
    @SuppressLint("StaticFieldLeak")
    private val instance = TrusToken() // Singleton instance of TrusToken

    /**
     * Detects if a USB security token is connected to the device.
     *
     * @param context The application context.
     * @return True if a USB token is detected, false otherwise.
     */
    fun detectUSB(context: Context): Boolean {
        return instance.detectUSB(context)
    }

    /**
     * Authenticates a user using the security token.
     *
     * @param context The application context.
     * @param pin The PIN entered by the user for authentication.
     * @return True if login is successful, false otherwise.
     */
    fun loginUser(context: Context, pin: String): Boolean {
        return instance.loginUser(context, pin)
    }

    /**
     * Signs a transaction using the security token.
     *
     * @param context The application context.
     * @param transactionString The transaction data that needs to be signed.
     * @return The signed transaction data.
     */
    fun signTransaction(context: Context, transactionString: String): String {
        return instance.signTransactionData(context, transactionString)
    }

    /**
     * Verifies the integrity of a signed transaction.
     * Currently, this method always returns true since public key verification is not implemented.
     *
     * @param sign The signed transaction data.
     * @param transactionString The original transaction data.
     * @return Always returns true (verification not implemented) because it requires senders public key to verify
     * the transaction data as public key is hard coded int the c++ library.
     */
    fun verifyToken(sign: String, transactionString: String): Boolean {
        return true
    }

    /**
     * Logs out the user from the USB security token.
     *
     * @return True if logout is successful, false otherwise.
     */
    fun logoutUSB(): Boolean {
        return instance.logoutUSB()
    }

    /**
     * Encrypts the given data using the security token.
     * This will be used to encrypt the transaction object json string and then it will save
     * to the database but currently it is not used due to some errors (Failed to initialized encryption)
     *
     * @param data The plain text data to be encrypted.
     * @return The encrypted data as a string.
     *
     */
    fun encrypt(data: String): String {
        return instance.encryptData(data)
    }

    /**
     * Decrypts the given encrypted data using the security token.
     * This will be used to decrypt the transaction object json string from the database
     * but currently it is not used due to some errors.
     *
     * @param data The encrypted data to be decrypted.
     * @return The decrypted plain text data.
     */
    fun decrypt(data: String): String {
        return instance.decryptData(data)
    }
}
