package com.example.memoi

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.memoi.databinding.ActivityMainBinding
import com.example.memoi.todo.Todo
import com.example.memoi.viewmodel.TodoListViewModel
import java.time.LocalDate
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var fragStack: Stack<Fragment>
    val vm : TodoListViewModel by viewModels()

    // without pushing to stack
    private fun jumpToFragment(frg: Fragment) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragStack = Stack<Fragment>()

        //// testing code... by hjk
        //todoListViewModel=ViewModelProvider(this).get(TodoListViewModel::class.java)
        var todolist = vm.getList()
        if(todolist.size!=0){
        for(i:Int in 0..todolist.size){
            if(todolist[i].localDate.equals(LocalDate.now())){
                var todo2=todolist[i]
                notificate(todo2)
            }
        }}
        //// until here...

        vm.foo()

        val navcon = binding.frgNav.getFragment<NavHostFragment>().navController
        binding.bottomNav.setupWithNavController(navcon)

        jumpToFragment(MainTodayFragment())
    }

    override fun onBackPressed() {
        if (fragStack.empty()) {super.onBackPressed()}
        else { exitFragment() }
    }

    override fun onNavigateUp(): Boolean {
        val navcon = binding.frgNav.getFragment<NavHostFragment>().navController

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