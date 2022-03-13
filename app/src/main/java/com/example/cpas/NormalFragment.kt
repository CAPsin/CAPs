package com.example.cpas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class NormalFragment : Fragment() {

    private lateinit var database : DatabaseReference
    private lateinit var array : ArrayList<Posting>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.normal_fragment, container, false)

        array = ArrayList()
        val rv = view.findViewById<RecyclerView>(R.id.rv_posting2)
        database = FirebaseDatabase.getInstance().getReference("Postings")
        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                array.clear()
                for(data in snapshot.children) {
                    if(data.child("type").value as String == "normal") {
                        val title = data.child("title").value as String
                        val content = data.child("content").value as String
                        val time = data.child("time").value as String
                        val commentNum = data.child("commentNum").value
                        val image = R.drawable.comment
                        val who = data.child("who").value as String

                        array.add(0, Posting(title, content, time, commentNum.toString(), image, who))
                    }
                }
                rv.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(view.context, "게시글을 불러 오지못했습니다", Toast.LENGTH_SHORT).show()
            }

        })

        rv.layoutManager = LinearLayoutManager(null)
        rv.setHasFixedSize(true)
        rv.adapter = PostingAdapter(array)



        return view
    }

}