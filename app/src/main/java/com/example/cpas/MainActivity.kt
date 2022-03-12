package com.example.cpas

import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var flag : String = "job"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //코드작성 예시
        //테스트2

        val search : ImageView = findViewById(R.id.iv_search)
        val more : ImageView = findViewById(R.id.iv_more)
        val category : ImageView = findViewById(R.id.iv_category)

        val comunity : Button = findViewById(R.id.btn_comunity)
        val planner : Button = findViewById(R.id.btn_planner)
        val myInfo : Button = findViewById(R.id.btn_myInfo)
        
        myInfo.setOnClickListener {
            val intent = Intent(this,MyinfoActivity::class.java)
            startActivity(intent)
        }
        more.setOnClickListener {
            val drawer : DrawerLayout = findViewById(R.id.layout_drawer)
            drawer.openDrawer(GravityCompat.END)
        }
        category.setOnClickListener {
            val drawer : DrawerLayout = findViewById(R.id.layout_drawer)
            drawer.openDrawer(GravityCompat.START)
        }
        val nav : NavigationView = findViewById(R.id.naviView)
        nav.setNavigationItemSelectedListener(this)

        val nav_category :NavigationView = findViewById(R.id.navi_category)
        nav_category.setNavigationItemSelectedListener(this)

        val write : Button = findViewById(R.id.btn_write)
        write.setOnClickListener {
            val intent1 = Intent(this, WritingActivity::class.java)
            intent1.putExtra("flag", flag)
            intent1.putExtra("id", intent.getStringExtra("id"))
            intent1.putExtra("who", intent.getStringExtra("who"))
            startActivity(intent1)
        }
        val job : Button = findViewById(R.id.btn_job)
        val normal : Button = findViewById(R.id.btn_normal)

        setFrag(0)

        job.setOnClickListener { setFrag(0) }
        normal.setOnClickListener { setFrag(1) }
    }

    private fun setFrag(fragNum : Int) {
        val ft = supportFragmentManager.beginTransaction()
        when(fragNum) {
            0 -> {
                ft.replace(R.id.main_frame, JobFragment()).commit()
                flag = "job"
            }
            1 -> {
                ft.replace(R.id.main_frame, NormalFragment()).commit()
                flag = "normal"
            }
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.tmp1 -> {
                val intent1 = Intent(this, WritingActivity::class.java)
                intent1.putExtra("flag", flag)
                intent1.putExtra("id", intent.getStringExtra("id"))
                intent1.putExtra("who", intent.getStringExtra("who"))
                startActivity(intent1)
                Toast.makeText(applicationContext, "글쓰기", Toast.LENGTH_SHORT).show()
            }
            R.id.tmp2 -> Toast.makeText(applicationContext, "임시", Toast.LENGTH_SHORT).show()
            R.id.tmp3 -> Toast.makeText(applicationContext, "임시", Toast.LENGTH_SHORT).show()
        }
        val drawer : DrawerLayout = findViewById(R.id.layout_drawer)
        drawer.closeDrawers()

        return false
    }

    override fun onBackPressed() {
        val drawer : DrawerLayout = findViewById(R.id.layout_drawer)
        if(drawer.isDrawerOpen(GravityCompat.END)||drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers()
        }
        else {
            super.onBackPressed()
        }
    }
}