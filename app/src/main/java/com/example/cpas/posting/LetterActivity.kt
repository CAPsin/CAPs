package com.example.cpas.posting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cpas.R
import com.example.cpas.databinding.ActivityLetterBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

class LetterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLetterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLetterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
    }

    private fun initLayout() {
        binding.sendLetterButton.setOnClickListener {
            val toToken = intent.getStringExtra("toToken")!!
            val title = "쪽지가 도착했습니다"
            val body = binding.letterContent.text.toString()
            sendNotification(toToken, title, body)
            finish()
        }
    }

    private fun sendNotification(toToken : String, title : String, body : String) {
        Log.d("###InPosting", "send to : $toToken")
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        Thread {
            val client = OkHttpClient()
            val json = JSONObject()
            val dataJson = JSONObject()
            dataJson.put("title", title)
            dataJson.put("body", body)
            json.put("notification", dataJson)
            json.put("to", toToken)
            val requestBody = RequestBody.create(JSON, json.toString())
            val builder = Request.Builder()
                .header(
                    "Authorization",
                    "key=" + "AAAAnU4ihj8:APA91bGjEjBXIfTIbB6R1b5AlkIQNh-t7hS_vlGkhd3wC9Cjz0ayOSJOXeQhFk84bbW6ADWbhvfK3PTS5Kj-R5DvTgrWGrcWk5VPDL2JdagCwX0OqE_lIo3MoG9NKs_4fc72ATNtS9pK"
                )
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build()
            val response = client.newCall(builder).execute()
            val finalResponse = response.body!!.string()
        }.start()
    }
}