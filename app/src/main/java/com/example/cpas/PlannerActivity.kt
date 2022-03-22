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
import java.util.*

class PlannerActivity : AppCompatActivity() {
    var dateString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planner)

        //추가 버튼
        val addPlanButton = findViewById<AppCompatButton>(R.id.addPlanButton)
        //달력 버튼
        val calendarButton = findViewById<ImageButton>(R.id.calendarButton)

        addPlanButton.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
                Log.d("TAG", dateString) //날짜 선택 후 확인 눌렀을 때 Dialog 나오도록 함
                val alert = AlertDialog.Builder(this) // 임시 명준이 dialog
                alert.setTitle("할 일 추가")
                val edittext = EditText(this)

                alert.setPositiveButton("추가") {
                        dialog, which -> Toast.makeText(applicationContext, "추가되었습니다",Toast.LENGTH_LONG).show()
                        Log.d("TAHG", edittext.text.toString())
                }
                alert.setNeutralButton("취소",null)

                alert.setView(edittext)
                alert.create()
                alert.show()
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()

        }

        calendarButton.setOnClickListener {
            Log.d("TAG", "임시 달력 리스너")
        }
    }
}