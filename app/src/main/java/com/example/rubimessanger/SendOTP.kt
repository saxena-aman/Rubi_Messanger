package com.example.rubimessanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rubimessanger.R
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import android.content.Intent
import android.view.View
import android.widget.Button
import com.example.rubimessanger.VerifyOTP
import java.util.concurrent.TimeUnit

class SendOTP : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_otp)
        val inputMobile = findViewById<EditText>(R.id.inputMobile)
        val buttonGetOTP = findViewById<Button>(R.id.buttonGetOTP)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        buttonGetOTP.setOnClickListener(View.OnClickListener {
            if (inputMobile.text.toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(this@SendOTP, "Enter Mobile Number", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            progressBar.visibility = View.VISIBLE
            buttonGetOTP.visibility = View.INVISIBLE
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + inputMobile.text.toString(),
                60,
                TimeUnit.SECONDS,
                this@SendOTP,
                object : OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                        progressBar.visibility = View.GONE
                        buttonGetOTP.visibility = View.VISIBLE
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        progressBar.visibility = View.GONE
                        buttonGetOTP.visibility = View.VISIBLE
                        Toast.makeText(this@SendOTP, e.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        forceResendingToken: ForceResendingToken
                    ) {
                        progressBar.visibility = View.GONE
                        buttonGetOTP.visibility = View.VISIBLE
                        val intent = Intent(applicationContext, VerifyOTP::class.java)
                        intent.putExtra("mobile", inputMobile.text.toString())
                        intent.putExtra("verificationId", verificationId)
                        startActivity(intent)
                    }
                }
            )
        })
    }
}