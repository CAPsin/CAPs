package com.example.cpas.searching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cpas.R
import com.example.cpas.posting.Posting
import com.example.cpas.posting.PostingAdapter
import com.google.firebase.database.*

class SearchActivity : AppCompatActivity() {

    private lateinit var database : DatabaseReference
    private lateinit var array : ArrayList<Posting>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val search_edittext = findViewById<EditText>(R.id.search_edittext)
        val search_complete_btn = findViewById<AppCompatButton>(R.id.search_complete_btn)

        array = ArrayList()
        val rv = findViewById<RecyclerView>(R.id.searched_recycler)
        database = FirebaseDatabase.getInstance().getReference("Postings")

        search_complete_btn.setOnClickListener {
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    array.clear()
                    for(data in snapshot.children) {
                        if((data.child("title").value as String).contains(search_edittext.text)) {
                            val id = data.child("id").value as String
                            val title = data.child("title").value as String
                            val content = data.child("content").value as String
                            val time = data.child("time").value as String
                            val commentNum = data.child("commentNum").value
                            val image = R.drawable.comment
                            val who = data.child("who").value as String
                            val epoch = data.child("epoch").value as String
                            val postingID = data.child("postingID").value as String

                            array.add(Posting(id, "test", title, content, time, commentNum.toString(), who, image, epoch, postingID))
                            Log.d("TAG", title)
                        }
                    }
                    if(array.size > 1) {
                        array.sortWith(Comparator { p0, p1 -> p0!!.epoch!!.toLong().compareTo(p1!!.epoch!!.toLong()) * -1})
                    }
                    rv.adapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "게시글을 불러 오지못했습니다", Toast.LENGTH_SHORT).show()
                }
            })
        }
        rv.layoutManager = LinearLayoutManager(null)
        rv.setHasFixedSize(true)
        rv.adapter = PostingAdapter(array, "test", "TEST")

    }
}