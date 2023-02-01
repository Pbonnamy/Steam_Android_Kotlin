package com.example.steamlike

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.steamlike.api.ApiClient
import com.example.steamlike.api.model.request.UserSigninRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {
    private var loginBtn: Button? = null
    private var registerBtn: Button? = null
    private var forgotPasswordBtn: TextView? = null
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.loginBtn = findViewById(R.id.loginBtn)
        this.registerBtn = findViewById(R.id.registerBtn)
        this.forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn)
        this.emailInput = findViewById(R.id.email)
        this.passwordInput = findViewById(R.id.password)
        this.progressBar = findViewById(R.id.progressBar)

        this.loginBtn?.setOnClickListener {
            val request = UserSigninRequest(
                email = this.emailInput?.text.toString(),
                password = this.passwordInput?.text.toString()
            )
            this.signin(request)
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

    private fun signin(request: UserSigninRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                startLoading()
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.authSignin(request) }

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()

                    val prefs: SharedPreferences = getSharedPreferences("values", MODE_PRIVATE)
                    prefs.edit().putString("token", "Bearer " + content?.accessToken).apply()

                    stopLoading()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    stopLoading()
                    Toast.makeText(this@LoginActivity, "Identifiants invalides", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                stopLoading()
                Toast.makeText(this@LoginActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startLoading() {
        progressBar?.visibility = ProgressBar.VISIBLE
        loginBtn?.isEnabled = false
        registerBtn?.isEnabled = false
        forgotPasswordBtn?.isEnabled = false
    }

    private fun stopLoading() {
        progressBar?.visibility = ProgressBar.GONE
        loginBtn?.isEnabled = true
        registerBtn?.isEnabled = true
        forgotPasswordBtn?.isEnabled = true
    }
}