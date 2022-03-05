package com.example.cpas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton

class AssignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign)

        val assignbtn = findViewById<AppCompatButton>(R.id.Assignbtn)
        val assname = findViewById<EditText>(R.id.AssName)
        val assemail = findViewById<EditText>(R.id.AssEmail)
        val assnick = findViewById<EditText>(R.id.AssNick)
        val assid = findViewById<EditText>(R.id.AssId)
        val asspass = findViewById<EditText>(R.id.AssPass)

        assignbtn.setOnClickListener{
            //TODO 파이어베이스에 데이터 저장
        }
    }
}