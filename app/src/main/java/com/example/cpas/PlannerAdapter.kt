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
import com.google.firebase.database.*
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.CoroutineContext


class PlannerAdapter(private val context: Context, private val plannerActivity: PlannerActivity) :
    RecyclerView.Adapter<PlannerAdapter.CustomViewHolder>() {
    var planList = mutableListOf<String>()
    private lateinit var userId : String
    private var auth : FirebaseAuth = FirebaseAuth.getInstance() // 파이어 베이스 형식
    private var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.uid.toString())

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button = itemView.findViewById<TextView>(R.id.addPlanButton)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlannerAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlannerAdapter.CustomViewHolder, position: Int) {
        holder.button.text = planList[position]
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userId = snapshot.child("id").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        holder.itemView.setOnClickListener {
            Log.d("Tag", planList[position] + planList.size)
            if (position == planList.size - 1) {
                val intent = Intent(context, SetPlanActivity::class.java)
                intent.putExtra("id", userId)
                ActivityCompat.startActivityForResult(holder.itemView.context as Activity,intent,101,null)
            }
        }
    }

    override fun getItemCount(): Int {
        return planList.size
    }
}