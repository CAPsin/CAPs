package com.example.cpas.profile

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
        val chageNick : ImageView = findViewById(R.id.changeNick)
        val mastersay : Button = findViewById(R.id.masterSay)


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
        // 문의하기
        mastersay.setOnClickListener {
            sendEmail()
            finish()
        }
        myposting.setOnClickListener {//내가 쓴 글 모아보기
            val intent1 = Intent(this, MypostingFragment::class.java)
            intent1.putExtra("id", intent.getStringExtra("id"))
            intent1.putExtra("who", intent.getStringExtra("who"))
            startActivity(intent1)
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
                    FirebaseDatabase.getInstance().getReference("Users").child(auth.uid.toString()).removeValue()
                    FirebaseDatabase.getInstance().getReference("Tokens").child(auth.uid.toString()).removeValue()
//                    moveTaskToBack(true);						// 태스크를 백그라운드로 이동
//                    finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
//                    android.os.Process.killProcess(android.os.Process.myPid());	// 앱 프로세스 종료
                    ActivityCompat.finishAffinity(this);
                    System.exit(0)
                }
                .setNegativeButton("취소",null)
            checkbox.show()
        }

        revert.setOnClickListener {
            finish()
        }


    }
    private fun sendEmail() {

        var emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "kmj0973@naver.com", null))
        //emailIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        //emailIntent.putExtra(Intent.EXTRA_TEXT, content)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(emailIntent, "메일 전송하기"))
        } else {
            Toast.makeText(this, "메일을 전송할 수 없습니다", Toast.LENGTH_LONG).show()
        }

    }
}

