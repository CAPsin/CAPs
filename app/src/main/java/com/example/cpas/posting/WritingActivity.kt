package com.example.cpas.posting

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.gridlayout.widget.GridLayout
import com.example.cpas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WritingActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    private val toggleText = arrayListOf("여행", "반려동물", "외국어", "봉사활동", "자동차", "음악", "레저", "책", "스포츠", "미술/공예", "문화", "춤", "게임", "음식", "기타") // 토글 버튼들 id를 저장한 배열
    private val selected = arrayListOf(false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false) // 각 토글 버튼들이  선택 되었는지에 대한 정보를 저장 하는 배열, 처음엔 false고 선택하면 true로 바뀜 취소하면 다시 false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        auth = FirebaseAuth.getInstance()

        val post : Button = findViewById(R.id.btn_post)
        val title : EditText = findViewById(R.id.et_title)
        val content : EditText = findViewById(R.id.et_content)
        val back : ImageView = findViewById(R.id.iv_back)

        val btnAdd : ImageView = findViewById(R.id.iv_addCategory)
        val gridLayout : GridLayout = findViewById(R.id.gridLayout)

        btnAdd.setOnClickListener {
            val dialog = ChooseCategoryDialog(this)
            val layout = ConstraintLayout.LayoutParams (
                240,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            dialog.setOnClickListener(object : ChooseCategoryDialog.OnDialogClickListener {
                override fun onClicked(selectedIndex: ArrayList<Int>) {
                    gridLayout.removeAllViews()
                    for (index in 0..14)
                        selected[index] = false

                    for (index in selectedIndex) {
                        selected[index] = true
                        val categoryText = TextView(applicationContext)
                        categoryText.text = toggleText[index]
                        categoryText.textAlignment = View.TEXT_ALIGNMENT_CENTER
                        categoryText.background = ContextCompat.getDrawable(applicationContext, R.drawable.box_border)
                        categoryText.textSize = 20F
                        categoryText.layoutParams = layout
                        layout.setMargins(5, 0, 5, 5)

                        gridLayout.addView(categoryText)
                    }
                }
            })

            dialog.showDialog()
        }

//        for (index in 0..14) { // 토글 버튼에 이벤트 달기
//            val toggle : ToggleButton = findViewById(toggleID[index])
//
//            toggle.setOnClickListener {
//                if(selected[index]) // 선택을 취소함
//                    selected[index] = !selected[index]
//                else // 선택함
//                    selected[index] = !selected[index]
//            }
//        }

        post.setOnClickListener {
            if(title.text.toString().trim() == "") {
                Toast.makeText(applicationContext, "제목을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            else if(content.text.toString().trim() == "") {
                Toast.makeText(applicationContext, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                val id : String? = intent.getStringExtra("id")
                val type : String? = intent.getStringExtra("flag")
                val tmp : String = title.text.toString()
                val tmp2 : String = content.text.toString()
                val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
                val time : String = sdf.format(Date())
                val postingID = id + "@" + Date()
//                Toast.makeText(applicationContext, Date().time.toString(), Toast.LENGTH_SHORT).show()
                val commentNum = "0"
                val who : String? = intent.getStringExtra("who")

                database = FirebaseDatabase.getInstance().getReference("Postings")
                val posting = Posting(id, type, tmp, tmp2, time, commentNum, who, R.drawable.comment, currentTimeMillis().toString(), postingID)
                database.child(postingID).setValue(posting).addOnSuccessListener {
                    for (index in 0..14) { // 파이어베이스에 카테고리도 추가하기
                        if(selected[index]) {
//                            val toggle: ToggleButton = findViewById(toggleID[index])
                            database.child(postingID).child("category").child(index.toString()).setValue(toggleText[index])
                        }
                    }
                    Toast.makeText(applicationContext, "글을 업로드 하였습니다", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, "업로드에 실패 했습니다", Toast.LENGTH_SHORT).show()
                }

            }
        }

        back.setOnClickListener {
            finish()
        }
    }
}
