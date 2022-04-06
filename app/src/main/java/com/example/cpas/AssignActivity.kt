package com.example.cpas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class AssignActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth // 파이어 베이스 형식
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign)
        auth = FirebaseAuth.getInstance()
        //test yong code

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        val assignbtn = findViewById<AppCompatButton>(R.id.Assignbtn)
        val assname = findViewById<EditText>(R.id.AssName)
        val assemail = findViewById<EditText>(R.id.AssEmail)
        val assnick = findViewById<EditText>(R.id.AssNick)
        val assid = findViewById<EditText>(R.id.AssId)
        val asspass = findViewById<EditText>(R.id.AssPass)

        assignbtn.setOnClickListener{
            //TODO 파이어베이스에 데이터 저장
            auth!!.createUserWithEmailAndPassword(assemail.text.toString(), asspass.text.toString())
                .addOnCompleteListener(this){
                    if(it.isSuccessful){
                        //파이어 베이스에 유저 기본 정보 저장
                        var userprofile = User(assname.text.toString(), assemail.text.toString(), assnick.text.toString(), assid.text.toString(), asspass.text.toString())
                        databaseReference.child(auth.uid.toString()).setValue(userprofile)
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra("email", assemail.text.toString())
                        intent.putExtra("id", assid.text.toString())
                        intent.putExtra("name", assname.text.toString())
                        intent.putExtra("nickname", assnick.text.toString())
                        intent.putExtra("password", asspass.text.toString())
                        startActivity(intent)
                        finish()
                    }
                    else{
                        //TODO 실패시 해야 할 일.
                        Toast.makeText(this, "가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
}