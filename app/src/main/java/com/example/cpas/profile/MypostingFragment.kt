package com.example.cpas.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.cpas.R
import com.example.cpas.home.JobFragment
import com.example.cpas.home.NormalFragment
import com.example.cpas.home.PagerAdapter
import com.example.cpas.posting.Posting
import com.example.cpas.posting.PostingAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.comment_item.*


class MypostingFragment : AppCompatActivity(){
    var flag : String = "job"//ㅇㅇ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myposting_fragment)

        val viewPager : ViewPager = findViewById(R.id.viewPager)
        val tabLayout : TabLayout = findViewById(R.id.tabLayout)

        val pageradapter = PagerAdapter(supportFragmentManager)

        val jobFrag = MyJobFragment()
        val normalFrag = MyNormalFragment()
        val id = intent.getStringExtra("id")!!
        val nickname = intent.getStringExtra("who")!!
        val bundle = Bundle(2)
        bundle.putString("param1", nickname)
        bundle.putString("param2", id)

        jobFrag.arguments = bundle
        normalFrag.arguments = bundle

        pageradapter.addFragment(jobFrag)
        pageradapter.addFragment(normalFrag)

        viewPager.adapter = pageradapter
        tabLayout.setupWithViewPager(viewPager)
    }

//    private fun setFrag(fragNum : Int) {
//        val ft = supportFragmentManager.beginTransaction()
//        when(fragNum) {
//            0 -> {
//                ft.replace(R.id.main_frame, JobFragment(intent.getStringExtra("nickname")!!, intent.getStringExtra("id")!!)).commit()
//                flag = "job"
//            }
//            1 -> {
//                ft.replace(R.id.main_frame, NormalFragment(intent.getStringExtra("nickname")!!, intent.getStringExtra("id")!!)).commit()
//                flag = "normal"
//            }
//        }
//    }
}