package com.example.steamlike

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.steamlike.api.ApiClient
import com.example.steamlike.api.Request
import com.example.steamlike.api.model.request.UserSignupRequest
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private var loginBtn: Button? = null
    private var registerBtn: Button? = null
    private var forgotPasswordBtn: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        Log.i("start", "test")
        val test = Request()
        test.start()


        this.loginBtn = findViewById(R.id.loginBtn)
        this.registerBtn = findViewById(R.id.registerBtn)
        this.forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn)

        this.loginBtn?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        this.registerBtn?.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        this.forgotPasswordBtn?.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

    }


}