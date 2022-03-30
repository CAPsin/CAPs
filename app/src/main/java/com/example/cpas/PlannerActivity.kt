package com.example.cpas

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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

        auth = FirebaseAuth.getInstance()
        databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(auth.uid.toString())

        //추가 버튼
        //val addPlanButton = findViewById<AppCompatButton>(R.id.addPlanButton)
        //달력 버튼
        val calendarButton = findViewById<ImageButton>(R.id.calendarButton)
        //리사이클러 뷰
        var planListView = findViewById<RecyclerView>(R.id.planRecyclerView)

        planAdapter = PlannerAdapter(this, this)
        planListView.adapter = planAdapter
        planDataList.add("+")
        planAdapter.planList = planDataList




        calendarButton.setOnClickListener {
            Log.d("TAG", "임시 달력 리스너")
        }
    }

    fun upload(planData: planData) {
        databaseReference.child("plan").child(dateString).setValue(planData)
    }

}