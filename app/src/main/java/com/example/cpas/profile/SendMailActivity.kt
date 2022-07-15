package com.example.cpas.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.os.StrictMode
import android.se.omapi.Session
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cpas.R
import java.util.*
import javax.mail.*
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SendMailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sendemail)

        val post : Button = findViewById(R.id.btn_post)
        val title : EditText = findViewById(R.id.et_title)
        val content : EditText = findViewById(R.id.et_content)
        val back : ImageView = findViewById(R.id.iv_back)

        post.setOnClickListener {
            sendEmail(content.text.toString(),title.text.toString())
            finish()
        }

        back.setOnClickListener {
            finish()
        }
    }
    private fun sendEmail(content: String, title: String) {

        var emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "kmj0973@naver.com", null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        emailIntent.putExtra(Intent.EXTRA_TEXT, content)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(emailIntent, "메일 전송하기"))
        } else {
            Toast.makeText(this, "메일을 전송할 수 없습니다", Toast.LENGTH_LONG).show()
        }

    }
}