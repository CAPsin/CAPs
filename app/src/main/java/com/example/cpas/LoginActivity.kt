package com.example.cpas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val intent = Intent(this, MainActivity::class.java)
        val loginbtn = findViewById<AppCompatButton>(R.id.LoginButton)

        loginbtn.setOnClickListener {
            startActivity(intent)
            finish()
        }
    }
}