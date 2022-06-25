package com.example.cpas.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cpas.databinding.ActivityNotificationBinding
import com.example.cpas.posting.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NotificationActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var database : DatabaseReference
    lateinit var binding : ActivityNotificationBinding
    lateinit var array : ArrayList<Notification>
    lateinit var adapter : NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        array = ArrayList()
        val recyclerView = binding.notificationRv
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid).child("notification")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("####", "onDataChange")
                array.clear()
                for(data in snapshot.children) {
                    val comment = data.child("comment").value as String
                    val pTitle = data.child("ptitle").value as String
                    val pWho = data.child("pwho").value as String
                    val pContent = data.child("pcontent").value as String
                    val pID = data.child("pid").value as String
                    val cWho = data.child("cwho").value as String
                    val cID = data.child("cid").value as String
                    val time = data.child("time").value as String
                    val writerUid = data.child("writerUid").value as String
                    val commentID = data.child("commentID").value as String
                    val epoch = data.child("epoch").value as String

                    array.add(Notification(comment, pTitle, pWho, pContent, pID, cWho, cID, time, writerUid, commentID, epoch))
                }
                if(array.size >= 1) {
                    array.sortWith(Comparator { p0, p1 -> p0!!.epoch.toLong().compareTo(p1!!.epoch.toLong()) * -1})
                    binding.nothing.visibility = View.GONE
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@NotificationActivity, "알림을 불러 오지못했습니다", Toast.LENGTH_SHORT).show()
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(null)
        recyclerView.setHasFixedSize(true)
        adapter = NotificationAdapter(array)
        recyclerView.adapter = adapter

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}