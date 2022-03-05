package com.example.cpas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginbtn = findViewById<AppCompatButton>(R.id.LoginButton)
        val assignbtn = findViewById<AppCompatButton>(R.id.AssignButton)

        loginbtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        assignbtn.setOnClickListener{
            val intent = Intent(this, AssignActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}