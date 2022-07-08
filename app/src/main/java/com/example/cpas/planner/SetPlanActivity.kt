package com.example.cpas.planner

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.cpas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class SetPlanActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth // 파이어 베이스 형식
    private lateinit var databaseReference: DatabaseReference


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_plan)

        var id = intent.getStringExtra("id")

        auth = FirebaseAuth.getInstance()
        databaseReference =
            FirebaseDatabase.getInstance().getReference("Plan").child(id.toString())


        var dateString = ""
        val cal: DatePicker = findViewById(R.id.datepicker)
        val editplan: EditText = findViewById(R.id.editplan)
        val checkButton: ImageButton = findViewById(R.id.check_button)
        val cancleButton: ImageButton = findViewById(R.id.cancel_button)
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val time: String = sdf.format(Date())
        val nowdate = time.split("/")
        dateString = "${nowdate[0]}년 ${nowdate[1]}월 ${nowdate[2]}일"
        cal.setOnDateChangedListener { datePicker, year, month, day ->
            if (month + 1 < 10 && day < 10) {
                dateString = "${year}년 0${month + 1}월 0${day}일"
            } else if (month + 1 < 10) {
                dateString = "${year}년 0${month + 1}월 ${day}일"
            } else if (day < 10) {
                dateString = "${year}년 ${month + 1}월 0${day}일"
            } else {
                dateString = "${year}년 ${month + 1}월 ${day}일"
            }
            Log.d("TAG", dateString) //날짜 선택 후 확인 눌렀을 때 Dialog 나오도록 함
        }

        checkButton.setOnClickListener {
            if (editplan.text.toString().equals("")) {
                Toast.makeText(this, "할 일을 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                val data = mapOf<String, String>(
                    "title" to editplan.text.toString(),
                    "time" to "0",
                    "color" to (1..10).random().toString()
                )


                databaseReference.child(dateString).child(editplan.text.toString())
                    .updateChildren(data).addOnSuccessListener {
                        finish()

                }.addOnFailureListener {
                    Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
                }

            }
        }

        cancleButton.setOnClickListener {  // 뒤로가기 버튼 해당 엑티비티 종료
            finish()
        }

    }


}