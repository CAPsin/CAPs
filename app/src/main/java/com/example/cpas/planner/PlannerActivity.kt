package com.example.cpas.planner

import android.app.DatePickerDialog
import android.content.ClipData
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.View.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cpas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*


class PlannerActivity : AppCompatActivity() {
    var dateString = ""
    var year = 0
    var month = 0
    var day = 0
    lateinit var planAdapter : PlannerAdapter
    var planDataList = mutableListOf<String>()
    private lateinit var auth: FirebaseAuth // 파이어 베이스 형식
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planner)
        var include_planlist = arrayListOf<String>()

        var include_plan : TextView = findViewById(R.id.include_plan)
        val planGraph : View = findViewById(R.id.plannerGraph)
        val pickdate : TextView = findViewById(R.id.pickdate)
        var id = intent.getStringExtra("id")

        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val time : String = sdf.format(Date())
        val nowdates = time.split("/")
        var nowdate = "${nowdates[0]}년 ${nowdates[1]}월 ${nowdates[2]}일"
        year = nowdates[0].toInt()
        month = nowdates[1].toInt() - 1
        day = nowdates[2].toInt()
        pickdate.text = nowdate
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Plan").child(id.toString())


        //달력 버튼
        val calendarButton = findViewById<ImageButton>(R.id.calendarButton)
        //리사이클러 뷰
        var planListView = findViewById<RecyclerView>(R.id.planRecyclerView)

        //그리드 레이아웃을 위함
        val gridLayoutManager = GridLayoutManager(applicationContext, 4)
        gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        planListView.layoutManager = gridLayoutManager
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
                    Log.d("TAg", planDataList.toString())
                }
                planDataList.add("+")
                planListView.adapter?.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        calendarButton.setOnClickListener {
            val cal = Calendar.getInstance() // 캘린더 뷰
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                if (month + 1 < 10 && dayOfMonth < 10) {
                    dateString = "${year}년 0${month + 1}월 0${dayOfMonth}일"
                } else if (month + 1 < 10) {
                    dateString = "${year}년 0${month + 1}월 ${dayOfMonth}일"
                } else if (dayOfMonth < 10) {
                    dateString = "${year}년 ${month + 1}월 0${dayOfMonth}일"
                } else {
                    dateString = "${year}년 ${month + 1}월 ${dayOfMonth}일"
                }
                this.year = year
                this.month = month
                day = dayOfMonth
                 Log.d("TAG","날짜 : "+dateString)
                nowdate = dateString
                pickdate.text = nowdate

                databaseReference.child(nowdate).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        planDataList.clear()
                        for(data in snapshot.children){
                            planDataList.add(data.child("title").value.toString())
                            Log.d("TAg", planDataList.toString())
                        }
                        planDataList.add("+")
                        planListView.adapter?.notifyDataSetChanged()

                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })

            }
            DatePickerDialog(this, dateSetListener, year,month,day).show()
        }

        planGraph.setOnDragListener(OnDragListener { v, event -> // TODO Auto-generated method stub
            val action = event.action
            when (action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    Log.d("TAG","나감")
                }
                DragEvent.ACTION_DRAG_ENTERED -> { // 드래그 실패
                    Log.d("TAG","들어옴")
                }
                DragEvent.ACTION_DROP -> { // 드래그 성공
                    Log.d("TAG",event.clipData.getItemAt(0).text.toString())
                    include_planlist.add(event.clipData.getItemAt(0).text.toString())
                    var temptext = ""
                    for(i in 0 .. include_planlist.size - 1){
                        temptext += include_planlist.get(i)
                        temptext += ", "
                    }
                    include_plan.text = "현재 : " + temptext
                }
                DragEvent.ACTION_DRAG_ENDED -> {

                }
                else -> {
                }
            }
            true
        })
    }

}