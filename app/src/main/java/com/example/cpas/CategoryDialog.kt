package com.example.cpas

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_categorydialog.*

class CategoryDialog(context: Context)
{
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.activity_categorydialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false) // 다이얼로그 바깥 화면 눌렀을 때, 꺼지지 않음
        dialog.show()

        val edit_name = dialog.findViewById<EditText>(R.id.edittext) //에디트 받는 함수

        dialog.cancel_button.setOnClickListener { //취소 버튼
            dialog.dismiss()
        }

        dialog.ok_button.setOnClickListener { // 확인 버튼
            onClickListener.onClicked(edit_name.text.toString()) //에티트에서 받은 스트링을 전달
            dialog.dismiss()
        }

    }

    interface OnDialogClickListener
    {
        fun onClicked(name: String)
    }

}