package com.example.cpas

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.combineMeasuredStates

class PostingAdapter(val postingList:ArrayList<Posting>) : Adapter<PostingAdapter.CustomViewHolder>() {
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
    }

    override fun getItemCount(): Int {
        return postingList.size
    }
}