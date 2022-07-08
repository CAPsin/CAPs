package com.example.cpas.posting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cpas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_in_posting.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*

class InPosting : AppCompatActivity() {

    private lateinit var database : DatabaseReference
    private lateinit var database2 : DatabaseReference
    private lateinit var database3 : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var array : ArrayList<Comment>
    val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_posting)
        array = ArrayList()

        val tmp = intent.getStringExtra("isNotification")
        if(tmp != null) {
            val pID = tmp
            
        } else {
            val ptitle = intent.getStringExtra("ptitle")
            val pwho = intent.getStringExtra("pwho")
            val pcontent = intent.getStringExtra("pcontent")
            val pID = intent.getStringExtra("pID")!!
            val cwho = intent.getStringExtra("cWho")!!
            val cID = intent.getStringExtra("cID")!!
            val writerUid = intent.getStringExtra("writerUid")!!

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
            database = FirebaseDatabase.getInstance().getReference("Postings").child(pID)
            database2 = FirebaseDatabase.getInstance().getReference("Notifications").child(writerUid)
            database3 = FirebaseDatabase.getInstance().getReference("Tokens").child(writerUid)
            auth = FirebaseAuth.getInstance()
            var toToken = ""
            database3.get().addOnCompleteListener { work ->
                toToken = work.result!!.value.toString()
                Log.d("###InPosting", "toToken get complete!!")
            }
            Log.d("###InPosting", "Writer Info : $writerUid $pwho")
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
                        val who2 = data.child("who").value as String
                        val id = data.child("id").value as String
                        val content2 = data.child("content").value as String
                        val time2 = data.child("time").value as String
                        val epoch = data.child("epoch").value as String
                        val commentID = data.child("commentID").value as String

                        array.add(Comment(who2, id, content2, time2, epoch, commentID))
                    }
                    if(array.size > 1) {
                        array.sortWith(Comparator { p0, p1 -> p0!!.epoch.toLong().compareTo(p1!!.epoch.toLong())})
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

            postPerson.setOnClickListener {
                val intent = Intent(this, LetterActivity::class.java)
                intent.putExtra("toToken", toToken)
                startActivity(intent)
            }

            postBtn.setOnClickListener {
                if(ccontent.text.toString().trim() == "") {
                    Toast.makeText(applicationContext, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
                }
                else {
                    val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
                    val time3 : String = sdf.format(Date())
                    val commentID : String = cID + "@" + Date()

                    val comment = Comment(cwho, cID, ccontent.text.toString(), time3, currentTimeMillis().toString(), commentID)
                    database.child("comment").child(commentID).setValue(comment).addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            database.child("commentNum").setValue(commentNum.toString())
                            rvParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                            rv.layoutParams = rvParams
                            var flag = 1
                            if(auth.currentUser!!.uid != writerUid) {
                                val notification = Notification(ccontent.text.toString(), ptitle!!, pwho!!, pcontent!!, pID, cwho, cID, time3, writerUid, commentID, currentTimeMillis().toString())
                                database2.child(commentID).setValue(notification).addOnCompleteListener {
                                    if(it.isSuccessful) {
                                        sendNotification(toToken, "$ptitle 글에 댓글이 달렸습니다", notification.comment)
                                    } else {
                                        Toast.makeText(applicationContext, "댓글을 게시하지 못했습니다", Toast.LENGTH_SHORT).show()
                                        flag = 0
                                    }
                                }
                            }
                            if(flag == 1) {
                                Toast.makeText(applicationContext, "댓글을 게시했습니다", Toast.LENGTH_SHORT).show()
                            }
                            ccontent.text.clear()
                            closeKeyboard()
                        } else {
                            Toast.makeText(applicationContext, "댓글을 게시하지 못했습니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun sendNotification(toToken : String, title : String, body : String) {
        Log.d("###InPosting", "send to : $toToken")
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        Thread {
            val client = OkHttpClient()
            val json = JSONObject()
            val dataJson = JSONObject()
            dataJson.put("title", title)
            dataJson.put("body", body)
            json.put("notification", dataJson)
            json.put("to", toToken)
            val requestBody = RequestBody.create(JSON, json.toString())
            val builder = Request.Builder()
                .header(
                    "Authorization",
                    "key=" + "AAAAnU4ihj8:APA91bGjEjBXIfTIbB6R1b5AlkIQNh-t7hS_vlGkhd3wC9Cjz0ayOSJOXeQhFk84bbW6ADWbhvfK3PTS5Kj-R5DvTgrWGrcWk5VPDL2JdagCwX0OqE_lIo3MoG9NKs_4fc72ATNtS9pK"
                )
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build()
            val response = client.newCall(builder).execute()
            val finalResponse = response.body!!.string()
        }.start()
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}