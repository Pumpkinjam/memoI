package com.example.memoi

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.memoi.databinding.ActivityMainBinding
import com.example.memoi.databinding.FragmentMainBinding
import com.example.memoi.todo.Todo
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var fragStack: Stack<Fragment>

    // without pushing to stack
    private fun jumpToFragment(frg: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(binding.frmFragment.id, frg)
            commit()
        }
        println("jumping to $frg")
    }

    fun goToFragment(frg: Fragment) {
        fragStack.push(binding.frmFragment.getFragment())
        jumpToFragment(frg)
    }

    fun exitFragment() = jumpToFragment(fragStack.pop())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragStack = Stack<Fragment>()
        // today todo list중 time 조건에 맞는 todo 가져오기
        // if .. 반복중 조건에 맞는 todo
        val todo2:Todo //입력
        val thisLocalTime: LocalDateTime = LocalDateTime.now()//조건 판별시 필요한 현재 시각
        //notificate(todo2) //조건애 맞는 todo 넘겨주면 작동
        jumpToFragment(MainFragment())
    }

    override fun onBackPressed() {
        if (fragStack.empty()) {super.onBackPressed()}
        else { exitFragment() }
    }

    override fun onNavigateUp(): Boolean {
        val navcon = binding.frmFragment.getFragment<MainFragment>()
            .binding.frgNav.getFragment<NavHostFragment>().navController

        return navcon.navigateUp() || super.onNavigateUp()
    }

    // todo: make it be able to be used generally
    fun notificate(todo: Todo) {

        val builder = NotificationCompat.Builder(this, "test_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("${todo.title}")
            .setContentText("${todo.description}\n${todo.url} ")
            .setDefaults(Notification.DEFAULT_VIBRATE)// 알림 진동기능
            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.calendar))//알림창 큰 아이콘
            .setAutoCancel(true)//

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 오레오 버전 이후에는 알림을 받을 때 채널이 필요
            // gradle에서 SDK 26 이상이 보장되므로 위 조건이 필요하지는 않음. 그래도 놔둡시다.
            val channel_id = "test_channel" // 알림을 받을 채널 id 설정
            val channel_name = "채널이름" // 채널 이름 설정
            val descriptionText = "설명글" // 채널 설명글 설정
            val importance = NotificationManager.IMPORTANCE_DEFAULT // 알림 우선순위 설정
            val channel = NotificationChannel(channel_id, channel_name, importance).apply {
                description = descriptionText
            }

            // 만든 채널 정보를 시스템에 등록
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            // 알림 표시: 알림의 고유 ID(ex: 1002), 알림 결과
            notificationManager.notify(1002, builder.build())
        }
    }
}