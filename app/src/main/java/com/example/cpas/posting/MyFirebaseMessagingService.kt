package com.example.cpas.posting

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.cpas.MainActivity
import com.example.cpas.R
import com.example.cpas.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val tmp = remoteMessage.notification!!.title!!
        val title = tmp.split("@#")[0]
//        val pID = tmp.split("@#")[1]
        val body = remoteMessage.notification!!.body!!
        sendNotification(title, body)
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Log.d("###Service", "New Token $p0")
    }

    fun sendNotification(title : String, body : String) {
        Log.d("###Service", "sendNotification started!!")
        val id = "MyChannel"
        val name = "NotificationChannel"
        val notificationChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableVibration(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val builder = NotificationCompat.Builder(this, id)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

//        val intent = Intent(this, InPosting::class.java)
//        intent.putExtra("isNotification", pID)
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        builder.setContentIntent(pendingIntent)
        val notification = builder.build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
        manager.notify(10, notification)
    }
}