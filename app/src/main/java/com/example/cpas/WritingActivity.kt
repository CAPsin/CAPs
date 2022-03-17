package com.example.cpas

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*

class WritingActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        auth = FirebaseAuth.getInstance()

        val post : Button = findViewById(R.id.btn_post)
        val title : EditText = findViewById(R.id.et_title)
        val content : EditText = findViewById(R.id.et_content)
        val back : ImageView = findViewById(R.id.iv_back)

        post.setOnClickListener {
            if(title.text.toString().trim() == "") {
                Toast.makeText(applicationContext, "제목을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            else if(content.text.toString().trim() == "") {
                Toast.makeText(applicationContext, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                val id : String? = intent.getStringExtra("id")
                val type : String? = intent.getStringExtra("flag")
                val tmp : String = title.text.toString()
                val tmp2 : String = content.text.toString()
                val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
                val time : String = sdf.format(Date())

//                Toast.makeText(applicationContext, Date().time.toString(), Toast.LENGTH_SHORT).show()
                val commentNum = "0"
                val who : String? = intent.getStringExtra("who")

                database = FirebaseDatabase.getInstance().getReference("Postings")
                val posting = Posting(id, type, tmp, tmp2, time, commentNum, who, R.drawable.comment, currentTimeMillis().toString())
                database.child((id+"@"+Date())).setValue(posting).addOnSuccessListener {
                    Toast.makeText(applicationContext, "글을 업로드 하였습니다", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, "업로드에 실패 했습니다", Toast.LENGTH_SHORT).show()
                }

            }
        }

        back.setOnClickListener {
            finish()
        }
    }
}
