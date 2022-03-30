package com.example.cpas

import android.content.ClipData
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var flag : String = "job"
    var isUp = false // 카테고리 애니메이션 상태표시

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //코드작성 예시
        //테스트2
        val search : ImageView = findViewById(R.id.iv_search)
        val more : ImageView = findViewById(R.id.iv_more)
        val category : ImageView = findViewById(R.id.iv_category)
        val category_plus : ImageButton = findViewById(R.id.btn_cateplus) //카테고리 플러스 버튼
        val category_line : LinearLayout = findViewById(R.id.category_line) //카테고리 삽입하기 위한 부모 레이어

        val category_scollview : HorizontalScrollView = findViewById(R.id.scollview) //카테고리 숨겨진 페이지
        val comunity : Button = findViewById(R.id.btn_comunity)
        val planner : Button = findViewById(R.id.btn_planner)
        val myInfo : Button = findViewById(R.id.btn_myInfo)

        val tranlateDownAnim : Animation // 카테고리 보여주는 애니메이션
        val tranlateUpAnim : Animation // 카테고리 숨기는 애니매이션

        tranlateDownAnim = AnimationUtils.loadAnimation(this,R.anim.translate_down);
        tranlateUpAnim = AnimationUtils.loadAnimation(this,R.anim.translate_up);
        
        category.setOnClickListener {//클릭시 애니메이션 작동 함수
            if(isUp){
                category_scollview.visibility = View.GONE
                category_scollview.startAnimation(tranlateUpAnim)
            }
            else{
                category_scollview.visibility = View.VISIBLE
                category_scollview.startAnimation(tranlateDownAnim)
            }
            isUp = !isUp
        }
        myInfo.setOnClickListener {
            val intent = Intent(this,MyinfoActivity::class.java)
            startActivity(intent)
        }
        more.setOnClickListener {
            val drawer : DrawerLayout = findViewById(R.id.layout_drawer)
            drawer.openDrawer(GravityCompat.END)
        }
<<<<<<< Updated upstream
        planner.setOnClickListener {
            val intent = Intent(this,PlannerActivity::class.java)
            startActivity(intent)
        }
        category_plus.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("카테고리 추가")
            alert.setMessage("원하시는 카테고리를 추가하십시오")
            val edittext = EditText(this)
            val category_text = TextView(this) //카테고리 추가 (텍스트가 들어가는지 확인하려고 버튼으로 설정함)


            alert.setPositiveButton("추가") {
                dialog, which -> Toast.makeText(applicationContext, "추가되었습니다",Toast.LENGTH_LONG).show()
                category_text.text = edittext.text.toString() //글씨 왜 안들어가는지 모르겠음 ;;;
                category_text.setTextSize(10F)
                category_text.setTextColor(Color.BLACK)
                category_line.addView(category_text)
            }
            alert.setNeutralButton("취소",null)

            alert.setView(edittext)
            alert.create()
            alert.show()
=======
        category_plus.setOnClickListener { //카테고리 추가 버튼
            val dialog = CategoryDialog(this) // 카테고리 다이얼로그 액티비티 받기
            val category_text = TextView(this)
            dialog.showDialog()
            dialog.setOnClickListener(object : CategoryDialog.OnDialogClickListener {
                override fun onClicked(name: String)
                {
                    category_text.text = name
                    category_text.setTextSize(10F)
                    category_text.setTextColor(Color.BLACK)
                    category_line.addView(category_text)
                }

            })
>>>>>>> Stashed changes
        }
        val nav : NavigationView = findViewById(R.id.naviView)
        nav.setNavigationItemSelectedListener(this)

        val write : Button = findViewById(R.id.btn_write)
        write.setOnClickListener {
            val intent1 = Intent(this, WritingActivity::class.java)
            intent1.putExtra("flag", flag)
            intent1.putExtra("id", intent.getStringExtra("id"))
            intent1.putExtra("who", intent.getStringExtra("nickname"))
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
                ft.replace(R.id.main_frame, JobFragment(intent.getStringExtra("nickname")!!, intent.getStringExtra("id")!!)).commit()
                flag = "job"
            }
            1 -> {
                ft.replace(R.id.main_frame, NormalFragment(intent.getStringExtra("nickname")!!, intent.getStringExtra("id")!!)).commit()
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
                intent1.putExtra("who", intent.getStringExtra("nickname"))
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