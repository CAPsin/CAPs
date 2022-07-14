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
            //sendEmail(content.text.toString(),title.text.toString())
            //val email = "a22950973@gmail.com"
            //GMailSender(content.text.toString(),title.text.toString()).sendEmail(email)
            Toast.makeText(applicationContext, "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        back.setOnClickListener {
            finish()
        }
    }
    private fun sendEmail(content: String, title: String) {
       // val emailAddress = "a22950973@gmail.com"

       // val intent = Intent(Intent.ACTION_SENDTO)
            //.apply {
              //  type = "text/plain" // 데이터 타입 설정
              //  data = Uri.parse("mailto:")

               // putExtra(Intent.EXTRA_EMAIL, emailAddress) // 메일 수신 주소 목록
               // putExtra(Intent.EXTRA_SUBJECT, title) // 메일 제목 설정
               // putExtra(Intent.EXTRA_TEXT, content) // 메일 본문 설정
            //}
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "a22950973@gmail.com", null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        emailIntent.putExtra(Intent.EXTRA_TEXT, content)
        startActivity(Intent.createChooser(emailIntent, ""))

    }
}