package com.example.cpas.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cpas.R
import com.example.cpas.posting.Posting
import com.example.cpas.posting.PostingAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.comment_item.*


class MypostingFragment : AppCompatActivity(){


    private lateinit var auth : FirebaseAuth
    private lateinit var database1 : DatabaseReference
    private lateinit var database : DatabaseReference
    private lateinit var array : ArrayList<Posting>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myposing_fragment)


        array = ArrayList()
        val rv = findViewById<RecyclerView>(R.id.myposting)
        auth = FirebaseAuth.getInstance()
        database1 = FirebaseDatabase.getInstance().getReference("Users")
        database = FirebaseDatabase.getInstance().getReference("Postings")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                array.clear()
                for(data in snapshot.children) {
                    if(data.child("type").value as String == "normal" && database1.child("id").toString() == data.child("who").value as String) {
                        val id = data.child("id").value as String
                        val title = data.child("title").value as String
                        val content = data.child("content").value as String
                        val time = data.child("time").value as String
                        val commentNum = data.child("commentNum").value
                        val image = R.drawable.comment
                        val who = data.child("who").value as String
                        val epoch = data.child("epoch").value as String
                        val postingID = data.child("postingID").value as String

                        array.add(Posting(id, "normal", title, content, time, commentNum.toString(), who, image, epoch, postingID))
                        rv.adapter = PostingAdapter(array, who, id)
                    }
                    if(data.child("type").value as String == "job" && database1.child("id").toString() == data.child("who").value as String) {
                        val id = data.child("id").value as String
                        val title = data.child("title").value as String
                        val content = data.child("content").value as String
                        val time = data.child("time").value as String
                        val commentNum = data.child("commentNum").value
                        val image = R.drawable.comment
                        val who = data.child("who").value as String
                        val epoch = data.child("epoch").value as String
                        val postingID = data.child("postingID").value as String

                        array.add(Posting(id, "job", title, content, time, commentNum.toString(), who, image, epoch, postingID))
                        rv.adapter = PostingAdapter(array, who, id)
                    }
                }
                if(array.size > 1) {
                    array.sortWith(Comparator { p0, p1 -> p0!!.epoch!!.toLong().compareTo(p1!!.epoch!!.toLong()) * -1})
                }
                rv.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(view.context, "게시글을 불러 오지못했습니다", Toast.LENGTH_SHORT).show()
            }
        })

        rv.layoutManager = LinearLayoutManager(null)
        rv.setHasFixedSize(true)

    }

}