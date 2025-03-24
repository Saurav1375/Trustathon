package com.example.trustoken_starter

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.trustoken_starter.auth.data.service.AuthUiClient
import com.example.trustoken_starter.auth.data.service.AuthWithUserPass
import com.example.trustoken_starter.core.navigation.NavigationGraph
import com.example.trustoken_starter.di.Injection
import com.example.trustoken_starter.ui.theme.TrusToken_StarterTheme
import com.google.android.gms.auth.api.identity.Identity
import java.nio.charset.StandardCharsets

class TrusToken : ComponentActivity() {
    private val authUiClient by lazy {
        AuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext),
            Injection.instance()
        )
    }

    private val authWithUserPass by lazy {
        AuthWithUserPass(
            Injection.instance()
        )
    }
    private lateinit var btnDetectToken: Button
    private lateinit var btnLogin: Button
    private lateinit var btnSign: Button
    private lateinit var btnVerify: Button
    private lateinit var btnEncrypt: Button
    private lateinit var btnDecrypt: Button
    private lateinit var btnLogout: Button
    private lateinit var btnClear: Button
    private lateinit var btnReadCertificate: Button

    private lateinit var tvTokenName: TextView
    private lateinit var tvSignature: TextView
    private lateinit var tvEncryptedData: TextView
    private lateinit var tvCertificate: TextView

    private lateinit var edtPin: EditText
    private lateinit var edtPlainText: EditText
    private lateinit var edtPlainText2: EditText

    private var fileDescriptor: Int = 0
    private var isTokenConnected = false
    private var tokenPin: String = ""
    private var plainText: String = ""

    companion object {
        init {
            System.loadLibrary("native-lib")
        }

        private const val ACTION_USB_PERMISSION = "com.example.USB_PERMISSION"

        fun hexStringToByteArray(s: String): ByteArray {
            return s.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        }

        fun byteArrayToAsciiString(bytes: ByteArray?): String {
            return bytes?.toString(StandardCharsets.US_ASCII) ?: ""
        }

        fun detectToken(context: Context): Boolean {
            val trusToken = TrusToken()
            return trusToken.detectSmartCard(context).let {
                trusToken.libint(it) == 0
            }
        }
    }

    private fun isHexString(str: String): Boolean {
        return str.matches(Regex("[0-9A-Fa-f]+"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrusToken_StarterTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavigationGraph(
                        navController = navController,
                        authUiClient = authUiClient,
                        authWithUserPass = authWithUserPass,
                        applicationContext = applicationContext,
                    )
                }
            }
        }
//        setContentView(R.layout.activity_trus_token)
//
//        btnDetectToken = findViewById(R.id.detect_token)
//        btnLogin = findViewById(R.id.login)
//        btnSign = findViewById(R.id.sign)
//        btnVerify = findViewById(R.id.verify)
//        btnEncrypt = findViewById(R.id.encrypt)
//        btnDecrypt = findViewById(R.id.decrypt)
//        btnLogout = findViewById(R.id.logout)
//        btnClear = findViewById(R.id.clear_token)
//
//        tvTokenName = findViewById(R.id.token_name)
//        tvSignature = findViewById(R.id.signature)
//        tvEncryptedData = findViewById(R.id.cipher_text)
//
//        edtPin = findViewById(R.id.token_pin)
//        edtPlainText = findViewById(R.id.plain_text)
//        edtPlainText2 = findViewById(R.id.plain_text2)
//        btnReadCertificate = findViewById(R.id.read_certificate)
//        tvCertificate = findViewById(R.id.certificate)
//        tvSignature.setTextIsSelectable(true)
//        tvEncryptedData.setTextIsSelectable(true)
//        tvCertificate.setTextIsSelectable(true)
//
//        btnReadCertificate.setOnClickListener {
//            if (isTokenConnected) {
//                val certificate = readCertificate()
//                tvCertificate.text = certificate
//                Toast.makeText(this, "Certificate read", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, "Please connect token first", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        btnDetectToken.setOnClickListener {
//            fileDescriptor = detectSmartCard()
//            if (libint(fileDescriptor) == 0) {
//                tvTokenName.text = "Trustoken"
//                isTokenConnected = true
//            }
//            Toast.makeText(this, "File Descriptor: $fileDescriptor", Toast.LENGTH_SHORT).show()
//        }
//
//        btnLogin.setOnClickListener {
//            if (isTokenConnected && edtPin.text.toString().isNotEmpty()) {
//                tokenPin = edtPin.text.toString()
//                println("Token Pin: $tokenPin")
//                val res = login(tokenPin)
//                println("Login Response: $res")
//                Toast.makeText(this, res, Toast.LENGTH_LONG).show()
//            }
//        }
//
//        btnSign.setOnClickListener {
//            if (isTokenConnected && edtPlainText.text.toString().isNotEmpty()) {
//                plainText = edtPlainText.text.toString()
//                tvSignature.text = signData()
//            } else {
//                Toast.makeText(this, "Fill all the required fields", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        btnVerify.setOnClickListener {
//            if(tvSignature.text.toString().isNotEmpty() && isHexString(tvSignature.text.toString()))
//                tvSignature.text = verify(tvSignature.text.toString(), edtPlainText.text.toString())
//        }
//
//        btnEncrypt.setOnClickListener {
//            if (isTokenConnected && edtPlainText2.text.toString().isNotEmpty()) {
//                plainText = edtPlainText2.text.toString()
//                tvEncryptedData.text = encrypt()
//            }
//        }
//
//        btnDecrypt.setOnClickListener {
//            if (isTokenConnected && tvEncryptedData.text.toString().isNotEmpty() && isHexString(tvEncryptedData.text.toString()))
//            tvEncryptedData.text = byteArrayToAsciiString(hexStringToByteArray(decrypt(tvEncryptedData.text.toString())))
//        }
//
//        btnLogout.setOnClickListener {
//            val res = logout()
////            val msg = if (res) "Logout Successful" else "Logout Failed"
//            Toast.makeText(this,res , Toast.LENGTH_LONG).show()
//        }
//
//        btnClear.setOnClickListener {
//            edtPin.text.clear()
//            edtPlainText.text.clear()
//            edtPlainText2.text.clear()
//            tvSignature.text = ""
//            tvEncryptedData.text = ""
//            tvCertificate.text = ""
//        }
    }

    fun detectSmartCard(context: Context = this): Int {
        try {
            val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager?
            usbManager?.deviceList?.values?.forEach { device ->
                if (isSmartCardReader(device)) {
                    val flag = if (Build.VERSION.SDK_INT >= 33) PendingIntent.FLAG_IMMUTABLE else 0
                    val permissionIntent = PendingIntent.getBroadcast(
                        context, 0, Intent(
                            ACTION_USB_PERMISSION
                        ), flag
                    )
                    usbManager.requestPermission(device, permissionIntent)
                    if (usbManager.hasPermission(device)) {
                        return getFileDescriptor(usbManager, device)
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                e.message.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
        return -1

    }

    private fun isSmartCardReader(device: UsbDevice): Boolean {
        return if (device.vendorId == 10381 && device.productId == 64) {
//            tvTokenName.text = "Trustoken"
            true
        } else false
    }

    private fun getFileDescriptor(manager: UsbManager, device: UsbDevice): Int {
        return manager.openDevice(device)?.fileDescriptor ?: -1
    }

//    fun getTokenPin(): String {
//        return token_pin
//    }

    fun getPlainText(): String {
        return plainText
    }


    fun detectUSB(context: Context): Boolean {
        fileDescriptor = detectSmartCard(context)
        if (libint(fileDescriptor) == 0) {
            return true
        } else return false
    }

    fun loginUser(context: Context, pin: String): Boolean {
        return when (login(pin)) {
            "Login Success" -> true
            else -> false
        }

    }

    fun signTransactionData(context: Context, transactionString: String): String {
        try {
            plainText = transactionString
            return signData()
        } catch (e: Exception) {
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
            return e.message.toString()
        }

    }

    fun verifyToken(sign: String, transactionString: String): Boolean {

        if (sign.isNotEmpty() &&
            isHexString(sign)
        ) {
            return when (verify(sign, transactionString)) {
                "Verified" -> true
                else -> false
            }
        }
        return false
    }

    fun logoutUSB() : Boolean {
        val res = logout()
        return res == "Logged out Successfully"
    }

    fun encryptData(data : String) : String {
        plainText = data
        return encrypt()
    }

    fun decryptData(data: String) : String {
          return byteArrayToAsciiString(hexStringToByteArray(decrypt(data)))
    }

    //    external fun loadLibrary(libPath: String): Boolean
//    external fun openSession(): Boolean
    external fun libint(int: Int): Int
    external fun login(tokenPin: String): String
    external fun signData(): String
    external fun verify(string: String, plainText: String): String
    external fun encrypt(): String
    external fun decrypt(string: String): String
    external fun logout(): String
    external fun readCertificate(): String
}
