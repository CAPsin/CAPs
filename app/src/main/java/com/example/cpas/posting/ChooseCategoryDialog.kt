package com.example.cpas.posting

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.WindowManager
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.cpas.R
import kotlinx.android.synthetic.main.activity_choose_category_dialog.*
import kotlinx.android.synthetic.main.activity_set_plan.view.*

class ChooseCategoryDialog(context : Context) : AppCompatActivity() {

    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    private val toggleID = arrayListOf(R.id.dialog_toggleButton1, R.id.dialog_toggleButton2, R.id.dialog_toggleButton3, R.id.dialog_toggleButton4, R.id.dialog_toggleButton5,
        R.id.dialog_toggleButton6, R.id.dialog_toggleButton7, R.id.dialog_toggleButton8, R.id.dialog_toggleButton9, R.id.dialog_toggleButton10,
        R.id.dialog_toggleButton11, R.id.dialog_toggleButton12, R.id.dialog_toggleButton13, R.id.dialog_toggleButton14, R.id.dialog_toggleButton15) // 토글 버튼들 id를 저장한 배열
    private val selected = arrayListOf(false, false, false, false, false, false, false, false, false,
        false, false, false, false, false, false) // 각 토글 버튼들이  선택 되었는지에 대한 정보를 저장 하는 배열, 처음엔 false고 선택하면 true로 바뀜 취소하면 다시 false
    private val selectedIndex = ArrayList<Int>()

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.activity_choose_category_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false) // 다이얼로그 바깥 화면 눌렀을 때, 꺼지지 않음

        for (index in 0..14) { // 토글 버튼에 이벤트 달기
            val toggle : ToggleButton = dialog.findViewById(toggleID[index])

            toggle.setOnClickListener {
                if(selected[index]) { // 선택을 취소함
                    selected[index] = !selected[index]
//                    val button = dialog.findViewById<ToggleButton>(toggleID[index])
//                    button.setTextColor(Color.BLACK)
//                    button.background = ContextCompat.getDrawable(parent.applicationContext, R.drawable.box_border)
                    toggle.setTextColor(Color.BLACK)
                    toggle.setBackgroundColor(Color.WHITE)
                }
                else { // 선택함
                    selected[index] = !selected[index]
//                    val button = dialog.findViewById<ToggleButton>(toggleID[index])
//                    button.setTextColor(Color.WHITE)
//                    button.background = ContextCompat.getDrawable(parent.applicationContext, R.drawable.box_black)
                    toggle.setTextColor(Color.WHITE)
                    toggle.setBackgroundColor(Color.BLACK)
                }
            }
        }

        dialog.btn_cancel.setOnClickListener { //취소 버튼
            dialog.dismiss()
        }

        dialog.btn_ok.setOnClickListener { // 확인 버튼
            for (index in 0..14) {
                if(selected[index])
                    selectedIndex.add(index)
            }
            onClickListener.onClicked(selectedIndex)
            dialog.dismiss()
        }

        dialog.show()
    }

    interface OnDialogClickListener
    {
        fun onClicked(selectedIndex : ArrayList<Int>)
    }

}