package com.example.cpas

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.cpas.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 3000 // 1 sec
    private lateinit var auth : FirebaseAuth // 파이어 베이스 형식
    private lateinit var databaseReference: DatabaseReference
    public lateinit var  context_main : Context
    public var userInfoArr = arrayOfNulls<String>(5)

    //데이터 가져오기 형식 보기 쉽게 영단어로 표현
    private var email = 0
    private var id = 1
    private var name = 2
    private var nickname = 3
    private var password = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        context_main = this
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.uid.toString())
        if(auth.uid == null){
            Handler().postDelayed({
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, SPLASH_TIME_OUT)
        }
        else{
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userInfoArr[email] = snapshot.child("email").value.toString()
                    userInfoArr[id] = snapshot.child("id").value.toString()
                    userInfoArr[name] = snapshot.child("name").value.toString()
                    userInfoArr[nickname] = snapshot.child("nickname").value.toString()
                    userInfoArr[password] = snapshot.child("password").value.toString()
                    nextActivity()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }

    }

    fun nextActivity(){
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.putExtra("email", userInfoArr[email])
            intent.putExtra("id", userInfoArr[id])
            intent.putExtra("name", userInfoArr[name])
            intent.putExtra("nickname", userInfoArr[nickname])
            intent.putExtra("password", userInfoArr[password])
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }

    public fun getData(i : Int): String? {
        return userInfoArr[i]
    }
}