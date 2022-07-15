package com.example.cpas.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.cpas.MainActivity
import com.example.cpas.R
import com.example.cpas.assign.AssignActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth // 파이어 베이스 형식
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        val user_email = intent.getStringExtra("email")
        val user_id = intent.getStringExtra("id")
        val user_name = intent.getStringExtra("name")
        val user_nickname = intent.getStringExtra("nickname")
        val user_password = intent.getStringExtra("password")


        val inputid : EditText = findViewById(R.id.EditId)
        val inputpass : EditText = findViewById(R.id.EditPassword)
        val loginbtn = findViewById<AppCompatButton>(R.id.LoginButton)
        val assignbtn = findViewById<TextView>(R.id.AssignButton)

        loginbtn.setOnClickListener {
            //TODO 유저 데이터 정보를 가져와서 로그인 정보 확인

            Log.d("tag1", inputid.text.toString().equals(user_id).toString())
            Log.d("tag1", inputpass.text.toString().equals(user_password).toString())
            auth?.signInWithEmailAndPassword(inputid.text.toString(),inputpass.text.toString())
                ?.addOnCompleteListener {
                        task ->
                    if(task.isSuccessful) {
                        // Login, 아이디와 패스워드가 맞았을 때
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.uid.toString())
                        databaseReference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                intent.putExtra("id", snapshot.child("id").value.toString())
                                intent.putExtra("nickname", snapshot.child("nickname").value.toString())
                                intent.putExtra("email", snapshot.child("email").value.toString())
                                intent.putExtra("name", snapshot.child("name").value.toString())
                                intent.putExtra("password", snapshot.child("password").value.toString())
                                startActivity(intent)
                                finish()
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }

                        })
                    } else {
                        // Show the error message, 아이디와 패스워드가 틀렸을 때
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }

        assignbtn.setOnClickListener{
            val intent = Intent(this, AssignActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}