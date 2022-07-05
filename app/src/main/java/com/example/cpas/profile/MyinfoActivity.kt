package com.example.cpas.profile

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cpas.R
import com.example.cpas.assign.AssignActivity
import com.example.cpas.home.CategoryDialog
import com.example.cpas.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MyinfoActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference : DatabaseReference
    var userInfoArr = arrayOfNulls<String>(5)

    private var email = 0
    private var id = 1
    private var name = 2
    private var nickname = 3
    private var password = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myinfo)
        val revert : ImageView = findViewById(R.id.iv_revert)
        val myid : TextView = findViewById(R.id.myid)
        val myemail : TextView = findViewById(R.id.myemail)
        val mynick : TextView = findViewById(R.id.nickname)
        val myposting : Button = findViewById(R.id.myposting)
        val removeuser : Button = findViewById(R.id.removeUser)
        val logout : Button = findViewById(R.id.logout)
        val chageNick : Button = findViewById(R.id.changeNick)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.uid.toString())
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userInfoArr[email] = snapshot.child("email").value.toString()
                userInfoArr[id] = snapshot.child("id").value.toString()
                userInfoArr[nickname] = snapshot.child("nickname").value.toString()
                myid.text = userInfoArr[id]
                myemail.text = userInfoArr[email]
                mynick.text = userInfoArr[nickname]
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        chageNick.setOnClickListener {
            val dialog = ChangeNickDialog(this) // 카테고리 다이얼로그 액티비티 받기
            val category_text = TextView(this)
            dialog.showDialog()
            dialog.setOnClickListener(object : ChangeNickDialog.OnDialogClickListener {
                override fun onClicked(name: String)
                {
                    FirebaseDatabase.getInstance().getReference("Users").orderByChild("nickname").equalTo(name)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Toast.makeText(getApplicationContext(),"중복입니다.",Toast.LENGTH_LONG).show()

                                } else {
                                    FirebaseDatabase.getInstance().getReference("Users")
                                        .child(auth.uid.toString()).child("nickname").setValue(name)
                                    mynick.text = name
                                    Toast.makeText(getApplicationContext(),"변경되었습니다.",Toast.LENGTH_LONG).show()
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                            }
                        })
                }
            })
        }
        myposting.setOnClickListener {//내가 쓴 글 모아보기
            startActivity(Intent(this, MypostingFragment::class.java))
        }
        logout.setOnClickListener {//로그아웃
            auth.signOut()
            if(auth.currentUser == null){
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
        }

        removeuser.setOnClickListener {// 회원탈퇴
            val checkbox = AlertDialog.Builder(this)
                .setTitle("회원탈퇴")
                .setMessage("탈퇴하시겠습니까?")
                .setPositiveButton("확인") { dialog, which ->
                    FirebaseAuth.getInstance().currentUser?.delete() // 데이터베이스 이메일 삭제 부분
                    FirebaseDatabase.getInstance().getReference("dd").child(auth.uid.toString()).removeValue()
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }
                .setNegativeButton("취소",null)
            checkbox.show()
        }

        revert.setOnClickListener {
            finish()
        }


    }
}

