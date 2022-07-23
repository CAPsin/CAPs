package com.example.cpas.planner

import android.R.attr
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.lang.Math.*
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.sqrt


class CanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var list = arrayListOf<planData>()
    var drawlist = arrayListOf<ArcData>()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val paint = Paint()
        paint.setColor(Color.RED)
        var rect = RectF();
        rect.set(0f, 0f, 732f, 732f);


        for (i in 0..list.size - 1) {
            when (list[i].color.toInt()) {
                0 -> paint.setColor(Color.parseColor("#E2D2D2"))
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
            drawlist[i].draw(canvas, paint)

        }

        //우리 테스트 원의 크기 알기
        //Log.d("TAG", canvas?.width.toString() + " " + canvas?.height.toString())
    }


    fun redraw() {
        invalidate()
    }

    fun changeThisTime(timeset: String, name: String){
        for (i in 0..drawlist.size - 1) {
            if(drawlist[i].title.equals(name)){
                drawlist[i].changeTime(timeset)
            }
        }
    }

    fun addlist(test: planData) {
        list.add(test)
        drawlist.add(ArcData(test.title,test.time, this))
        //테스트용 로그 찍기
        for (i in 0..list.size - 1) {
            Log.d("TOAASD", list.get(i).toString())
        }
    }

    fun inthere(xpos : Double, ypos : Double) : String? {

        for(i in 0..drawlist.size -1 ){
             if(drawlist[i].inthere(xpos, ypos)){
                 return drawlist[i].title
             }
        }

        return null
    }

}

class ArcData(var title : String, var time : String, val canvasView: CanvasView){

    var timelist = time.split("@")
    var starttime = timelist[0]
    var endtime = timelist[1]

    var starttimelist = starttime.split("/")
    var endtimelist = endtime.split("/")

    var realStartTime = starttimelist[0].toDouble()*60 + starttimelist[1].toDouble()
    var realEndTime = endtimelist[0].toDouble()*60 + endtimelist[1].toDouble()

    var startRange = (realStartTime / 5 * 1.25).toFloat() - 90f
    var swipeAngle = realEndTime - realStartTime

    fun changeTime(timeset : String){
        time = timeset
    }

    fun draw(canvas: Canvas?, paint: Paint){
        var rect = RectF();
        rect.set(0f, 0f, 732f, 732f);
        if(realEndTime < realStartTime){
            canvas?.drawArc(rect, startRange,
                ((swipeAngle + 1440) / 5 * 1.25).toFloat(), true, paint)
        }
        else{
            canvas?.drawArc(rect, startRange,
                (swipeAngle / 5 * 1.25).toFloat(), true, paint)
        }
        canvasView.redraw()
    }

    fun inthere(xpos : Double, ypos : Double) :Boolean{
        //PI = 180도
        var x = 0.0 // 부채꼴 중심 벡터의 x좌표
        var y = 0.0 // 부채꼴 중심 백터의 y좌표
        if(realEndTime < realStartTime){
            x = 366 * cos(PI * ((startRange + ((swipeAngle + 1440) / 5 * 1.25) - (((swipeAngle + 1440) / 5 * 1.25) / 2)) /180))
            y = 366 * sin(PI * ((startRange + ((swipeAngle + 1440) / 5 * 1.25) - (((swipeAngle + 1440) / 5 * 1.25) / 2)) /180))
        }
        else{
            x = 366 * cos(PI * ( (startRange + (swipeAngle / 5 * 1.25) - ((swipeAngle / 5 * 1.25) / 2)) /180))
            y = 366 * sin(PI * ((startRange + (swipeAngle / 5 * 1.25) - ((swipeAngle / 5 * 1.25) / 2)) /180))
        }
       // var x = 366 * cos(PI / 6 / 2)
      //  var y = 366 * sin(PI / 6 / 2)
        var u: Double
        var v: Double
        var inner: Double
        var theta: Double

        if((Distance(xpos, ypos) >=366)){
            return false
        }
        else{
            // 두 벡터의 크기 u,v 구하기
            u = Vector_size(x.toDouble(), y.toDouble())
            v = Vector_size(xpos, ypos)

            // 두 벡터간의 내적 구하기
            inner = Inner_func(x.toDouble(), y.toDouble(), xpos, ypos)

            // 두 벡터간의 내적에 두 벡터의 크기를 곱한 것을 나누어 두 벡터간의 각도 구하기
            // acos함수는 구글링을..
            theta = acos(inner / (u * v))

            // 만약 두 벡터간의 각이 주어진 각보다 작다면, 이는 범위 내의 적으로 판단
            if(realEndTime < realStartTime){
                if (theta <= (PI * (((realEndTime +1440- realStartTime) / 5 * 1.25)/180) / 2)){
                    return true
                }
                else{
                    return false
                }
            }
            else{
                if (theta <= (PI * (((realEndTime - realStartTime) / 5 * 1.25)/180) / 2)){
                    return true
                }
                else{
                    return false
                }
            }
        }
    }

    fun Vector_size(A1: Double, A2: Double): Double {
        return sqrt(pow(A1, 2.0) + pow(A2, 2.0))
    }

    fun Inner_func(x: Double, y: Double, x1: Double, y1: Double): Double {
        return x * x1 + y * y1
    }

    fun Distance(x: Double, y: Double): Double {
        val distance: Double
        distance = sqrt(pow(x, 2.0) + pow(y, 2.0))
        return distance
    }

}