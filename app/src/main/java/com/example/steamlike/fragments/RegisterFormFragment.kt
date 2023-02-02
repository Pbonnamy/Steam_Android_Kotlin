package com.example.steamlike.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.steamlike.LoginActivity
import com.example.steamlike.R
import com.example.steamlike.api.ApiClient
import com.example.steamlike.api.model.request.UserSignupRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFormFragment: Fragment() {
    private var registerBtn: Button? = null
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null
    private var passwordConfirmInput: EditText? = null
    private var usernameInput: EditText? = null
    private var progressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_register_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.registerBtn = view.findViewById(R.id.subscribe)
        this.emailInput = view.findViewById(R.id.email)
        this.passwordInput = view.findViewById(R.id.password)
        this.usernameInput = view.findViewById(R.id.username)
        this.passwordConfirmInput = view.findViewById(R.id.verifyPassword)
        this.progressBar = view.findViewById(R.id.progressBar)

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
                Toast.makeText(context, "Les mots de passes sont différents", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signup(request: UserSignupRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                startLoading()
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.authSignup(request) }

                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(activity, "Inscription réussie", Toast.LENGTH_SHORT).show()

                    stopLoading()
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    stopLoading()
                    Toast.makeText(activity, "Champ(s) invalide(s)", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                stopLoading()
                Toast.makeText(activity, "Service indisponible", Toast.LENGTH_SHORT).show()
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