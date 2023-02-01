package com.example.steamlike

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.steamlike.api.ApiClient
import com.example.steamlike.api.model.request.UserSignupRequest
import kotlinx.coroutines.*

class RegisterActivity : AppCompatActivity() {
    private var registerBtn: Button? = null
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null
    private var passwordConfirmInput: EditText? = null
    private var usernameInput: EditText? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        this.registerBtn = findViewById(R.id.subscribe)
        this.emailInput = findViewById(R.id.email)
        this.passwordInput = findViewById(R.id.password)
        this.usernameInput = findViewById(R.id.username)
        this.passwordConfirmInput = findViewById(R.id.verifyPassword)
        this.progressBar = findViewById(R.id.progressBar)

        this.registerBtn?.setOnClickListener {
            if (this.passwordInput?.text.toString() == this.passwordConfirmInput?.text.toString()) {
                val request = UserSignupRequest(
                    username = this.usernameInput?.text.toString(),
                    email = this.emailInput?.text.toString(),
                    password = this.passwordInput?.text.toString(),
                    roles = listOf("ROLE_USER")
                )
                this.signup(request)
            } else {
                Toast.makeText(this, "Les mots de passes sont différents", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signup(request: UserSignupRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                startLoading()
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.authSignup(request) }

                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(this@RegisterActivity, "Inscription réussie", Toast.LENGTH_SHORT).show()

                    stopLoading()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    stopLoading()
                    Toast.makeText(this@RegisterActivity, "Champ(s) invalide(s)", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                stopLoading()
                Toast.makeText(this@RegisterActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startLoading() {
        this.progressBar?.visibility = ProgressBar.VISIBLE
        this.registerBtn?.isEnabled = false
    }

    private fun stopLoading() {
        this.progressBar?.visibility = ProgressBar.INVISIBLE
        this.registerBtn?.isEnabled = true
    }
}