package com.example.trustoken_starter.payment.data.service

import android.annotation.SuppressLint
import android.content.Context
import com.example.trustoken_starter.TrusToken


object TokenService {
    @SuppressLint("StaticFieldLeak")
    private val instance = TrusToken()

    fun detectUSB(context: Context): Boolean {
        return instance.detectUSB(context)
//        return true
    }


    fun loginUser(context: Context, pin: String): Boolean {
        return instance.loginUser(context, pin)
//        return true
    }

    fun signTransaction(context: Context, transactionString: String): String {
        return instance.signTransactionData(context, transactionString)
    }

    //For this method the public key of the sender is required so for now it will return true
    fun verifyToken(sign: String, transactionString: String): Boolean{
        return true
    }

    fun logoutUSB() : Boolean {
        return instance.logoutUSB()
    }

    fun encrypt(data : String) : String {
        return instance.encryptData(data)
    }

    fun decrpyt(data : String) : String {
        return instance.decryptData(data)
    }

}
