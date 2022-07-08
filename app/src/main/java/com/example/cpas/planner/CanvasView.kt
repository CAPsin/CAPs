package com.example.cpas.planner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View

class CanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var list = arrayListOf<planData>()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val paint = Paint()
        paint.setColor(Color.RED)
        var rect = RectF();
        rect.set(0f, 0f, 732f, 732f);


        for (i in 0.. list.size - 1){
            when (list[i].color.toInt()){
                0 ->paint.setColor(Color.parseColor("#E2D2D2"))
                1 -> paint.setColor(Color.parseColor("#E3E2B4"))
                2 -> paint.setColor(Color.parseColor("#A2B59F"))
                3 -> paint.setColor(Color.parseColor("#D18063"))
                4 -> paint.setColor(Color.parseColor("#A9CBD7"))
                5 -> paint.setColor(Color.parseColor("#FFDDFF"))
                6 -> paint.setColor(Color.parseColor("#DDFFF6"))
                7 -> paint.setColor(Color.parseColor("#CCDDEE"))
                8 -> paint.setColor(Color.parseColor("#BBBBBB"))
                9 -> paint.setColor(Color.parseColor("#EECCE5"))
                10 -> paint.setColor(Color.parseColor("#FFFFDD"))
            }
            canvas?.drawArc(rect, 270f + i.toFloat()*30f, 30f, true, paint)
        }

        //우리 테스트 원의 크기 알기
        Log.d("TAG", canvas?.width.toString() + " " + canvas?.height.toString())
    }

    fun redraw(){
        invalidate()
    }

    fun addlist(test : planData){
        list.add(test)
        //테스트용 로그 찍기
        for(i in 0 .. list.size - 1){
            Log.d("TOAASD", list.get(i).toString())
        }
    }


}