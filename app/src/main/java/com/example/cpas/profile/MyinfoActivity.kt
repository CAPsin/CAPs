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
            val dialog = ChangeNickDialog(this) // ???????????? ??????????????? ???????????? ??????
            val category_text = TextView(this)
            dialog.showDialog()
            dialog.setOnClickListener(object : ChangeNickDialog.OnDialogClickListener {
                override fun onClicked(name: String)
                {
                    FirebaseDatabase.getInstance().getReference("Users").orderByChild("nickname").equalTo(name)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Toast.makeText(getApplicationContext(),"???????????????.",Toast.LENGTH_LONG).show()

                                } else {
                                    FirebaseDatabase.getInstance().getReference("Users")
                                        .child(auth.uid.toString()).child("nickname").setValue(name)
                                    mynick.text = name
                                    Toast.makeText(getApplicationContext(),"?????????????????????.",Toast.LENGTH_LONG).show()
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                            }
                        })
                }
            })
        }
        // ????????????
        mastersay.setOnClickListener {
            sendEmail()
            finish()
        }
        myposting.setOnClickListener {//?????? ??? ??? ????????????
            val intent1 = Intent(this, MypostingFragment::class.java)
            intent1.putExtra("id", intent.getStringExtra("id"))
            intent1.putExtra("who", intent.getStringExtra("who"))
            startActivity(intent1)
        }
        logout.setOnClickListener {//????????????
            auth.signOut()
            if(auth.currentUser == null){
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
        }

        removeuser.setOnClickListener {// ????????????
            val checkbox = AlertDialog.Builder(this)
                .setTitle("????????????")
                .setMessage("?????????????????????????")
                .setPositiveButton("??????") { dialog, which ->
                    FirebaseAuth.getInstance().currentUser?.delete() // ?????????????????? ????????? ?????? ??????
                    FirebaseDatabase.getInstance().getReference("Users").child(auth.uid.toString()).removeValue()
                    FirebaseDatabase.getInstance().getReference("Tokens").child(auth.uid.toString()).removeValue()
//                    moveTaskToBack(true);						// ???????????? ?????????????????? ??????
//                    finishAndRemoveTask();						// ???????????? ?????? + ????????? ??????????????? ?????????
//                    android.os.Process.killProcess(android.os.Process.myPid());	// ??? ???????????? ??????
                    ActivityCompat.finishAffinity(this);
                    System.exit(0)
                }
                .setNegativeButton("??????",null)
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
            startActivity(Intent.createChooser(emailIntent, "?????? ????????????"))
        } else {
            Toast.makeText(this, "????????? ????????? ??? ????????????", Toast.LENGTH_LONG).show()
        }

    }
}

