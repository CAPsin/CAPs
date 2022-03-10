package com.example.cpas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val user_email = intent.getStringExtra("email")
        val user_id = intent.getStringExtra("id")
        val user_name = intent.getStringExtra("name")
        val user_nickname = intent.getStringExtra("nickname")
        val user_password = intent.getStringExtra("password")


        val inputid : EditText = findViewById(R.id.EditId)
        val inputpass : EditText = findViewById(R.id.EditPassword)
        val loginbtn = findViewById<AppCompatButton>(R.id.LoginButton)
        val assignbtn = findViewById<AppCompatButton>(R.id.AssignButton)

        loginbtn.setOnClickListener {
            //TODO 유저 데이터 정보를 가져와서 로그인 정보 확인

            Log.d("tag1", inputid.text.toString().equals(user_id).toString())
            Log.d("tag1", inputpass.text.toString().equals(user_password).toString())
            //Toast.makeText(this, user_id.toString(), Toast.LENGTH_SHORT).show()
            //Toast.makeText(this, user_password.toString(), Toast.LENGTH_SHORT).show()
            //inputid.setText(user_id.toString())
            //inputpass.setText(user_password.toString())
            if(inputid.text.toString().equals(user_id) && inputpass.text.toString().equals(user_password)){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else if(!(inputid.text.toString().equals(user_id))){
                Toast.makeText(this, "아이디가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            }
            else if(!(inputpass.text.toString().equals(user_password))){
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            }
        }

        assignbtn.setOnClickListener{
            val intent = Intent(this, AssignActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}