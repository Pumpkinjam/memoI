package com.example.memoi

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.PowerManager
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.memoi.databinding.ActivityMainBinding
import com.example.memoi.todo.Todo
import com.example.memoi.viewmodel.TodoListViewModel
import java.time.LocalTime
import java.util.*
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var fragStack: Stack<Fragment>
    val vm : TodoListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragStack = Stack<Fragment>()

        //절전모드예외 앱으로 해재하는 권한 얻는 코드() -> 없다면 절전모드로 인한 1분마다의 체크 불가.
        val pm:PowerManager = getApplicationContext().getSystemService(POWER_SERVICE) as PowerManager
        var isWhiteListing = false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName())
        }
        if (!isWhiteListing) {
            var intent = Intent()
            intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()))
            startActivity(intent)
        }


        val navcon = binding.frgNav.getFragment<NavHostFragment>().navController
        binding.bottomNav.setupWithNavController(navcon)

        //jumpToFragment(MainTodayFragment())
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            checkAlarm()
        }, 10000)


    }

    override fun onBackPressed() {
        if (fragStack.empty()) {super.onBackPressed()}
        else { exitFragment() }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navcon = binding.frgNav.getFragment<NavHostFragment>().navController

        return navcon.navigateUp() || super.onSupportNavigateUp()
    }

    // without pushing to stack
    fun jumpToFragment(frg: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(binding.frgNav.id, frg)
            commit()
        }
        println("jumping to $frg")
    }

    fun goToFragment(frg: Fragment) {
        fragStack.push(binding.frgNav.getFragment())
        jumpToFragment(frg)
    }

    fun exitFragment() = jumpToFragment(fragStack.pop())

    fun hideTray() {
        binding.bottomNav.visibility = View.INVISIBLE
    }

    fun showTray() {
        binding.bottomNav.visibility = View.VISIBLE
    }
     fun checkAlarm(){
        val timer = Timer()
        var todolist = vm.getTodayList()
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                if(todolist.size!=0){
                    for(i:Int in 0..todolist.size-1){
                        if(todolist[i].localTime.equals(LocalTime.now())){
                            var todo2=todolist[i]
                            notificate(todo2)
                        }
                        //this is test code
                        //var todo2=todolist[i]
                        //notificate(todo2)
                    }
                }
            }
        }
        timer.schedule(timerTask, 0, 60000)
    }

    // todo: make it be able to be used generally
    fun notificate(todo: Todo) {

        val builder = NotificationCompat.Builder(this, "test_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("${todo.title}")
            .setContentText("${todo.description}\n${todo.url} ")
            .setDefaults(Notification.DEFAULT_VIBRATE)// 알림 진동기능
            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.calendar))//알림창 큰 아이콘
            //.setAutoCancel(true)// 알람터치시 삭제... 작동 안하는 것으로 보임

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
