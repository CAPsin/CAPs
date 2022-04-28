package com.example.cpas

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.example.cpas.*
import com.example.cpas.home.CategoryDialog
import com.example.cpas.home.JobFragment
import com.example.cpas.home.NormalFragment
import com.example.cpas.home.PagerAdapter
import com.example.cpas.planner.PlannerActivity
import com.example.cpas.posting.WritingActivity
import com.example.cpas.profile.MyinfoActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
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

        val viewpager : ViewPager = findViewById(R.id.viewPager)
        val tab : TabLayout = findViewById(R.id.tabLayout)
        val category_scollview : HorizontalScrollView = findViewById(R.id.scollview) //카테고리 숨겨진 페이지

        val scaleDownAnim : Animation// 카테고리 보여주는 애니메이션
        val scaleUpAnim : Animation// 카테고리 숨기는 애니매이션

        scaleDownAnim = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleUpAnim = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        category.setOnClickListener {//클릭시 애니메이션 작동 함수
            if (isUp) {
                ObjectAnimator.ofFloat(viewpager, "translationY", -10f).apply {
                    duration = 500
                    start()
                }
                ObjectAnimator.ofFloat(tab, "translationY", -10f).apply {
                    duration = 500
                    start()
                }
                category_scollview.visibility = View.GONE
                category_scollview.startAnimation(scaleUpAnim)
            } else {
                ObjectAnimator.ofFloat(viewpager, "translationY", 140f).apply {
                    duration = 500
                    start()
                }
                ObjectAnimator.ofFloat(tab, "translationY", 140f).apply {
                    duration = 500
                    start()
                }
                category_scollview.visibility = View.VISIBLE
                category_scollview.startAnimation(scaleDownAnim)
            }
            isUp = !isUp
        }

        more.setOnClickListener {
            val drawer : DrawerLayout = findViewById(R.id.layout_drawer)
            drawer.openDrawer(GravityCompat.END)
        }
        category_plus.setOnClickListener { //카테고리 추가 버튼
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val dialog = CategoryDialog(this) // 카테고리 다이얼로그 액티비티 받기
            val category_text = TextView(this)
            dialog.showDialog()
            dialog.setOnClickListener(object : CategoryDialog.OnDialogClickListener {
                override fun onClicked(name: String)
                {
                    category_text.text = name
                    category_text.setTextSize(20F)
                    category_text.layoutParams = layoutParams
                    layoutParams.setMargins(changeDP(5), changeDP(12), changeDP(5), 0)
                    category_text.setTextColor(Color.BLACK)
                    //category_text.setCompoundDrawables(R.drawable.is_category_bolder,null,null,null)
                    category_line.addView(category_text)
                }

            })
        }
        val nav : NavigationView = findViewById(R.id.naviView)
        nav.setNavigationItemSelectedListener(this)

//        val job : Button = findViewById(R.id.btn_job)
//        val normal : Button = findViewById(R.id.btn_normal)
//
//        setFrag(0)
//
//        job.setOnClickListener { setFrag(0) }
//        normal.setOnClickListener { setFrag(1) }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener(this)


        val viewPager : ViewPager = findViewById(R.id.viewPager)
        val tabLayout : TabLayout = findViewById(R.id.tabLayout)

        val pageradapter = PagerAdapter(supportFragmentManager)
        pageradapter.addFragment(JobFragment(intent.getStringExtra("nickname")!!, intent.getStringExtra("id")!!))
        pageradapter.addFragment(NormalFragment(intent.getStringExtra("nickname")!!, intent.getStringExtra("id")!!))
        viewPager.adapter = pageradapter
        tabLayout.setupWithViewPager(viewPager)
    }
    private fun changeDP(value : Int) : Int{
        var displayMetrics = resources.displayMetrics
        var dp = Math.round(value * displayMetrics.density)
        return dp
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

            R.id.bottom_write -> {
                val intent1 = Intent(this, WritingActivity::class.java)
                intent1.putExtra("flag", flag)
                intent1.putExtra("id", intent.getStringExtra("id"))
                intent1.putExtra("who", intent.getStringExtra("nickname"))
                startActivity(intent1)
            }
            R.id.bottom_planner -> {
                val intent1 = Intent(this, PlannerActivity::class.java)
                intent1.putExtra("id", intent.getStringExtra("id"))
                startActivity(intent1)
                finish()
            }
            R.id.bottom_myinfo -> {
                val intent = Intent(this, MyinfoActivity::class.java)
                startActivity(intent)
            }
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
