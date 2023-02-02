package com.example.steamlike

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.steamlike.fragments.RegisterFormFragment

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, RegisterFormFragment())
            .commit()
    }
}