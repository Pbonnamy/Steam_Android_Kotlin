package com.example.steamlike

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.steamlike.fragments.ForgotPasswordFormFragment

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, ForgotPasswordFormFragment())
            .commit()
    }
}