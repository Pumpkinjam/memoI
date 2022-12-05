package com.example.memoi

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.memoi.todo.Todo

const val ALARM_NOTIFICATION_ID =0
class AlarmReceiver():BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {//null이 아닐시 실행.
            var contentIntent = Intent(context,MainActivity::class.java)
            var pendingIntent = PendingIntent.getActivity(context,0,contentIntent,
                PendingIntent.FLAG_IMMUTABLE)
            //입력값들 가져오기
            val title= intent?.getStringExtra("title")
            val description = intent?.getStringExtra("description")
            val url = intent?.getStringExtra("url")
            val notification = NotificationCompat.Builder(context, App.ALERT_CHANNL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("$title")
                .setContentText("$description\n$url")
                //.setDefaults(Notification.DEFAULT_VIBRATE)// 알림 진동기능
                .setContentIntent(pendingIntent)
                .build()
            context.getSystemService(NotificationManager::class.java)
                .notify(ALARM_NOTIFICATION_ID,notification)

        }
    }
}