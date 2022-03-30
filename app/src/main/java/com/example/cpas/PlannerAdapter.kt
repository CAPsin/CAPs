package com.example.cpas

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.CoroutineContext


class PlannerAdapter(private val context: Context, private val plannerActivity: PlannerActivity) :
    RecyclerView.Adapter<PlannerAdapter.CustomViewHolder>() {
    private lateinit var auth: FirebaseAuth // 파이어 베이스 형식
    private lateinit var databaseReference: DatabaseReference
    var planList = mutableListOf<String>()

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button = itemView.findViewById<TextView>(R.id.addPlanButton)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlannerAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_item, parent, false)
        auth = FirebaseAuth.getInstance()
        databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(auth.uid.toString())
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlannerAdapter.CustomViewHolder, position: Int) {
        holder.button.text = planList[position]
        var dateString: String = ""
        var plantitle: String = ""
        var flag: Boolean = false


        holder.itemView.setOnClickListener {
            Log.d("Tag", planList[position] + planList.size)
            if (position == planList.size - 1) {
                val cal = Calendar.getInstance()    //캘린더뷰 만들기
                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        dateString = "${year}년 ${month + 1}월 ${dayOfMonth}일"
                        Log.d("TAG", dateString) //날짜 선택 후 확인 눌렀을 때 Dialog 나오도록 함
                        val alert = AlertDialog.Builder(context) // 임시 명준이 dialog
                        alert.setTitle("할 일 추가")
                        val edittext = EditText(context)

                        alert.setPositiveButton("추가") { dialog, which ->
                            plantitle = edittext.text.toString()
                            planList.add(0, edittext.text.toString())
                            flag = true
                            notifyDataSetChanged()
                            val data = planData(plantitle, "0")
                            plannerActivity.upload(data)
//                            databaseReference.child("plan").child(dateString).setValue(data)
                            //done.await()
                        }
                        alert.setNeutralButton("취소", null)

                        alert.setView(edittext)
                        alert.create()
                        alert.show()
                    }
                DatePickerDialog(context,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            } else {
                return@setOnClickListener
            }

        }
    }

    override fun getItemCount(): Int {
        return planList.size
    }
}