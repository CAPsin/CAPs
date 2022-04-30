package com.example.cpas.posting

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.cpas.R

class PostingAdapter(val postingList:ArrayList<Posting>, val who : String, val id : String) : Adapter<PostingAdapter.CustomViewHolder>() {


    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val contentPreview = itemView.findViewById<TextView>(R.id.tv_contentPreview)
        val time = itemView.findViewById<TextView>(R.id.tv_time)
        val commentNum = itemView.findViewById<TextView>(R.id.tv_commentNum)
        val commentImage = itemView.findViewById<ImageView>(R.id.iv_comment)
        val who = itemView.findViewById<TextView>(R.id.tv_who)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.title.text = postingList.get(position).title
        var preView = postingList.get(position).content.replace('\n',' ')
        when {
            preView.length > 46 -> {
                val tmp = preView.substring(0..45) + ".."
                holder.contentPreview.text = tmp
            }
            else -> holder.contentPreview.text = preView
        }
        holder.time.text = postingList.get(position).time
        holder.commentNum.text = postingList.get(position).commentNum
        holder.commentImage.setImageResource(postingList.get(position).commentImage)
        holder.who.text = postingList.get(position).who

        holder.itemView.setOnClickListener {
            Log.d("Tag", postingList[position].title)
            val intent = Intent(holder.itemView.context, InPosting::class.java)
            intent.putExtra("ptitle", postingList[position].title)
            intent.putExtra("ptime", postingList[position].time)
            //intent.putExtra("pcontent", postingList[position].contentPreview)
            intent.putExtra("pcontent", postingList[position].content)
            intent.putExtra("pwho", postingList[position].who)
            intent.putExtra("pID", postingList[position].postingID)
            intent.putExtra("cWho", who)
            intent.putExtra("cID", id)
            startActivityForResult(holder.itemView.context as Activity,intent,101,null)
        }
    }

    override fun getItemCount(): Int {
        return postingList.size
    }
}