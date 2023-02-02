package com.example.steamlike.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.steamlike.LoginActivity
import com.example.steamlike.R
import com.example.steamlike.api.ApiClient
import com.example.steamlike.api.model.request.PasswordLostRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPasswordFormFragment : Fragment() {
    private var forgotPasswordBtn: Button? = null
    private var emailInput: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_forgot_password_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.forgotPasswordBtn = view.findViewById(R.id.sendForgotPasswordBtn)
        this.emailInput = view.findViewById(R.id.email)

        this.forgotPasswordBtn ?.setOnClickListener {
            val request = PasswordLostRequest(
                email = this.emailInput?.text.toString()
            )

            this.resetPassword(request)
        }
    }

    private fun resetPassword(request: PasswordLostRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) { ApiClient.apiService.lostPassword(request) }

                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(activity, getString(R.string.resetPassword), Toast.LENGTH_SHORT).show()

                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(activity, getString(R.string.userNotFoundError), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(activity, getString(R.string.apiError), Toast.LENGTH_SHORT).show()
            }
        }
    }
}