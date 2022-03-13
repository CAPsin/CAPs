package com.example.cpas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class InPosting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_posting)

        val ptitle = intent.getStringExtra("ptitle")
        val ptime = intent.getStringExtra("ptime")
        val pwho = intent.getStringExtra("pwho")
        val pcontent = intent.getStringExtra("pcontent")

        val title : TextView = findViewById(R.id.postingTitle)
        val who : TextView = findViewById(R.id.postPerson)
        val content : TextView = findViewById(R.id.postContent)

        title.text = ptitle
        who.text = pwho
        content.text = pcontent
        //val time : TextView = findViewById(R.id.postTime)
    }
}