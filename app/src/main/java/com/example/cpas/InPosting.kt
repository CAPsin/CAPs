package com.example.cpas

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*

class InPosting : AppCompatActivity() {

    private lateinit var database : DatabaseReference
    private lateinit var array : ArrayList<Comment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_posting)
        array = ArrayList()

        val ptitle = intent.getStringExtra("ptitle")
        val ptime = intent.getStringExtra("ptime")
        val pwho = intent.getStringExtra("pwho")
        val pcontent = intent.getStringExtra("pcontent")
        val pID = intent.getStringExtra("pID")
        val cwho = intent.getStringExtra("cWho")
        val cID = intent.getStringExtra("cID")

        val title : TextView = findViewById(R.id.postingTitle)
        val who : TextView = findViewById(R.id.postPerson)
        val content : TextView = findViewById(R.id.postContent)
        val time : TextView = findViewById(R.id.postingTime)
        val postBtn : Button = findViewById(R.id.postCommentButton)
        val ccontent : EditText = findViewById(R.id.postComment)
        val rv : RecyclerView = findViewById(R.id.rv_comment)

        title.text = ptitle
        who.text = pwho
        content.text = pcontent

        //firebase에서 글 정보 읽어오기
        var commentNum : Long = 0
        database = FirebaseDatabase.getInstance().getReference("Postings").child(pID!!)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                time.text = snapshot.child("time").value.toString()
                commentNum = snapshot.child("comment").childrenCount
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "게시글을 불러 오지못했습니다", Toast.LENGTH_SHORT).show()
            }
        })
        //firebase에서 댓글 정보 읽어오기
        database.child("comment").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                array.clear()
                for(data in snapshot.children) {
                    val who = data.child("who").value as String
                    val id = data.child("id").value as String
                    val content = data.child("content").value as String
                    val time = data.child("time").value as String
                    val epoch = data.child("epoch").value as String
                    val commentID = data.child("commentID").value as String

                    array.add(Comment(who, id, content, time, epoch, commentID))
                }
                if(array.size > 1) {
                    array.sortWith(Comparator { p0, p1 -> p0!!.epoch!!.toLong().compareTo(p1!!.epoch!!.toLong())})
                }
                rv.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "댓글을 불러 오지못했습니다", Toast.LENGTH_SHORT).show()
            }
        })

        rv.layoutManager = LinearLayoutManager(null)
        rv.setHasFixedSize(true)
        rv.adapter = CommentAdapter(array)

        //title과 content의 크기 조정
        val display = windowManager.defaultDisplay

        val contentParams = content.layoutParams
        contentParams.width = display.width - display.width/12
        content.layoutParams = contentParams

        val titleParams = title.layoutParams
        titleParams.width = display.width - display.width/12
        title.layoutParams = titleParams

        val rvParams = rv.layoutParams
        rvParams.width = display.width - display.width/12
        rvParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        rv.layoutParams = rvParams

        postBtn.setOnClickListener {
            if(ccontent.text.toString().trim() == "") {
                Toast.makeText(applicationContext, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
                val time : String = sdf.format(Date())
                val commentID : String = cID + "@" + Date()

                val comment = Comment(cwho!!, cID!!, ccontent.text.toString(), time, currentTimeMillis().toString(), commentID)
                database.child("comment").child(commentID).setValue(comment).addOnSuccessListener {
                    database.child("commentNum").setValue(commentNum.toString())
                    rvParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    rv.layoutParams = rvParams
                    ccontent.text.clear()
                    closeKeyboard()
                }.addOnCanceledListener {
                    Toast.makeText(applicationContext, "댓글을 게시하지 못했습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}