package com.example.steamlike

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.steamlike.api.ApiClient
import com.example.steamlike.api.model.request.UserSigninRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private var loginBtn: Button? = null
    private var registerBtn: Button? = null
    private var forgotPasswordBtn: TextView? = null
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        this.loginBtn = findViewById(R.id.loginBtn)
        this.registerBtn = findViewById(R.id.registerBtn)
        this.forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn)
        this.emailInput = findViewById(R.id.email)
        this.passwordInput = findViewById(R.id.password)

        this.loginBtn?.setOnClickListener {
            val request = UserSigninRequest(
                username = this.emailInput?.text.toString(),
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
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.authSignin(request)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()

                    val prefs: SharedPreferences = getSharedPreferences("values", MODE_PRIVATE)
                    prefs.edit().putString("token", "Bearer " + content?.accessToken).apply()

                    Toast.makeText(this@LoginActivity, "Connexion réussie", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LoginActivity, "Identifiants invalides", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }
}