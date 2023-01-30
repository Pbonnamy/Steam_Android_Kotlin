package com.example.steamlike

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.steamlike.api.ApiClient
import com.example.steamlike.api.model.request.UserSignupRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private var registerBtn: Button? = null
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null
    private var passwordConfirmInput: EditText? = null
    private var usernameInput: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resgister_activity)

        this.registerBtn = findViewById<Button>(R.id.subscribe)
        this.emailInput = findViewById<EditText>(R.id.email)
        this.passwordInput = findViewById<EditText>(R.id.password)
        this.usernameInput = findViewById<EditText>(R.id.username)
        this.passwordConfirmInput = findViewById<EditText>(R.id.verifyPassword)

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
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.authSignup(request)

                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(this@RegisterActivity, "Inscription réussie", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@RegisterActivity, "Champ(s) invalide(s)", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Service indisponible", Toast.LENGTH_SHORT).show()
            }
        }
    }
}