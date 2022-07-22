package com.example.cpas.planner

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.WindowManager
import android.widget.NumberPicker
import android.widget.TimePicker
import com.example.cpas.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.settimedialog.*
import java.util.*

const val Time_Interval = 5

class SetTimeDialog (context: Context){
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener
    private var start_time = ""
    private var end_time = ""
    private var change_time = ""
    private var flag = true // true 가 시작 시간, false가 끝 시간


    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.settimedialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false) // 다이얼로그 바깥 화면 눌렀을 때, 꺼지지 않음
        dialog.show()

        var cal = Calendar.getInstance()
        val hours = cal.get(Calendar.HOUR_OF_DAY)
        val minutes = cal.get(Calendar.MINUTE)
        change_time = "${hours}/${minutes}"

        val timePicker : TimePicker = dialog.findViewById(R.id.timespinner) // 타임 스피너
        setTimePickerInterval(timePicker)
        timePicker.setOnTimeChangedListener { timePicker, hour, minute ->
            Log.d("TAGME", "현재 설정된 시간은 ${hour} : ${minute * Time_Interval}")
            change_time = "${hour}/${minute * Time_Interval}"
        }

        val timetablayout : TabLayout = dialog.findViewById(R.id.timetablayout)
        timetablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0->{ // 시작 시간
                        flag = true
                        end_time = change_time
                    }
                    1-> { // 끝 시간
                        flag = false
                        start_time = change_time
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //TODO("Not yet implemented")
            }

        })
        dialog.time_cancle_button.setOnClickListener { //취소 버튼
            dialog.dismiss()
        }

        dialog.time_ok_button.setOnClickListener { // 확인 버튼
            if(flag){
                start_time = change_time
            }
            else{
                end_time = change_time
            }
            onClickListener.onClicked("${start_time}@${end_time}") //에티트에서 받은 스트링을 전달
            dialog.dismiss()
        }

    }

    interface OnDialogClickListener
    {
        fun onClicked(time: String)
    }


    private fun setTimePickerInterval(timePicker: TimePicker) {
        try {
            val TIME_PICKER_INTERVAL = 5
            val minutePicker = timePicker.findViewById(
                Resources.getSystem().getIdentifier(
                    "minute", "id", "android"
                )
            ) as NumberPicker
            minutePicker.minValue = 0
            minutePicker.maxValue = 60 / TIME_PICKER_INTERVAL - 1
            val displayedValues: MutableList<String> = ArrayList()
            var i = 0
            while (i < 60) {
                displayedValues.add(String.format("%02d", i))
                i += TIME_PICKER_INTERVAL
            }
            minutePicker.displayedValues = displayedValues.toTypedArray()
        } catch (e: Exception) {
        }
    }


}