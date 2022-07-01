package com.example.cpas.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.cpas.R
import com.example.cpas.posting.InPosting
import com.example.cpas.posting.Notification
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NotificationAdapter(private val notificationList : ArrayList<Notification>) : Adapter<NotificationAdapter.CustomViewHolder>() {

    lateinit var database : DatabaseReference

    fun removeItem(pos : Int) {
        database.child(notificationList[pos].commentID).removeValue()
//        database.child(notificationList[pos].commentID).removeValue().addOnCompleteListener {
//            if(it.isSuccessful) {
//                notifyItemRemoved(pos)
//            }
//        }
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.notificationTitle)
        val content : TextView = itemView.findViewById(R.id.notificationContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        database = FirebaseDatabase.getInstance().getReference("Notifications").child(notificationList[position].writerUid)

        var title = if(notificationList[position].pTitle.length > 15) notificationList[position].pTitle.substring(0..14) + ".." else notificationList[position].pTitle
        title = "'$title' 글에 댓글이 달렸습니다"
        holder.title.text = title

        val tempContent = notificationList[position].comment
        val content = if(tempContent.length > 20) tempContent.substring(0..20) + ".." else tempContent
        holder.content.text = content

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, InPosting::class.java)
            intent.putExtra("ptitle", notificationList[position].pTitle)
            intent.putExtra("ptime", notificationList[position].time)
            intent.putExtra("pcontent", notificationList[position].pContent)
            intent.putExtra("pwho", notificationList[position].pWho)
            intent.putExtra("pID", notificationList[position].pID)
            intent.putExtra("writerUid", notificationList[position].writerUid)
            intent.putExtra("cWho", notificationList[position].cWho)
            intent.putExtra("cID", notificationList[position].cID)
            database.child(notificationList[position].commentID).removeValue().addOnCompleteListener {
                if(it.isSuccessful) {
                    startActivityForResult(holder.itemView.context as Activity, intent, 102, null)
                } else {
                    Toast.makeText(holder.itemView.context, "문제가 발생했습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show()
                }
            }
//            startActivityForResult(holder.itemView.context as Activity, intent, 102, null)
        }
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }
}