package com.example.steamlike

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.steamlike.fragments.LoginFormFragment


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, LoginFormFragment())
            .commit()
    }
}