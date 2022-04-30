package com.example.cpas.planner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cpas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class PlannerActivity : AppCompatActivity() {
    var dateString = ""
    lateinit var planAdapter : PlannerAdapter
    var planDataList = mutableListOf<String>()
    private lateinit var auth: FirebaseAuth // 파이어 베이스 형식
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planner)

        val pickdate : TextView = findViewById(R.id.pickdate)
        var id = intent.getStringExtra("id")

        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val time : String = sdf.format(Date())
        val nowdates = time.split("/")
        var nowdate = "${nowdates[0]}년 ${nowdates[1]}월 ${nowdates[2]}일"
        pickdate.text = nowdate
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Plan").child(id.toString())


        //달력 버튼
        val calendarButton = findViewById<ImageButton>(R.id.calendarButton)
        //리사이클러 뷰
        var planListView = findViewById<RecyclerView>(R.id.planRecyclerView)

        planAdapter = PlannerAdapter(this, this)
        planListView.adapter = planAdapter
        planDataList.add("+")
        planAdapter.planList = planDataList
        val data = mapOf<String, String>(
            "title" to "이거테스트",
            "time" to "0"
        )

        databaseReference.child(nowdate).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                planDataList.clear()
                for(data in snapshot.children){
                    planDataList.add(data.child("title").value.toString())
                }
                planDataList.add("+")
                planListView.adapter?.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        calendarButton.setOnClickListener {
            Log.d("TAG", "임시 달력 리스너")
            databaseReference.child("plan").child(dateString).child("test")
                .updateChildren(data)
        }
    }

}