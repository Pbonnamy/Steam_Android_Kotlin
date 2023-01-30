package com.example.steamlike

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.steamlike.api.ApiClient
import com.example.steamlike.api.model.request.PasswordLostRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {
    private var forgotPasswordBtn: Button? = null
    private var emailInput: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password_activity)

        this.forgotPasswordBtn = findViewById(R.id.sendForgotPasswordBtn)
        this.emailInput = findViewById(R.id.email)

        this.forgotPasswordBtn ?.setOnClickListener {
            val request = PasswordLostRequest(
                email = this.emailInput?.text.toString()
            )

            this.resetPassword(request)
        }
    }

    private fun resetPassword(request: PasswordLostRequest) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.lostPassword(request)

                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(this@ForgotPasswordActivity, "Mot de passe réinitialisé", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@ForgotPasswordActivity, "Utilisateur introuvable", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ForgotPasswordActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }
}