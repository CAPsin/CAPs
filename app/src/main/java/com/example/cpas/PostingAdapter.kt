package com.example.cpas

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.combineMeasuredStates

class PostingAdapter(val postingList:ArrayList<Posting>) : Adapter<PostingAdapter.CustomViewHolder>() {


    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val contentPreview = itemView.findViewById<TextView>(R.id.tv_contentPreview)
        val time = itemView.findViewById<TextView>(R.id.tv_time)
        val commentNum = itemView.findViewById<TextView>(R.id.tv_commentNum)
        val commentImage = itemView.findViewById<ImageView>(R.id.iv_comment)
        val who = itemView.findViewById<TextView>(R.id.tv_who)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostingAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostingAdapter.CustomViewHolder, position: Int) {
        holder.title.text = postingList.get(position).title
        holder.contentPreview.text = postingList.get(position).contentPreview
        holder.time.text = postingList.get(position).time
        holder.commentNum.text = postingList.get(position).commentNum
        holder.commentImage.setImageResource(postingList.get(position).commentImage)
        holder.who.text = postingList.get(position).who

        holder.itemView.setOnClickListener {
            Log.d("Tag", postingList[position].title)
            val intent = Intent(holder.itemView.context, InPosting::class.java)
            intent.putExtra("ptitle", postingList[position].title)
            intent.putExtra("ptime", postingList[position].time)
            intent.putExtra("pcontent", postingList[position].contentPreview)
            intent.putExtra("pwho", postingList[position].who)
            startActivityForResult(holder.itemView.context as Activity,intent,101,null)

        }
    }

    override fun getItemCount(): Int {
        return postingList.size
    }
}