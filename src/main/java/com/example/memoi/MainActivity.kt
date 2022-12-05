package com.example.memoi

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
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
import java.time.temporal.ChronoUnit
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

        jumpToFragment(MainTodayFragment())

        //핸들러를 통한 10초 딜레이 후 실행,viewmodel이 firebase로부터 객체를 로딩완료후 실행가능.
        Handler(Looper.getMainLooper()).postDelayed({

            //실행시점 기준 미래에 설정된 모든 알람 설정, 아직 기능 온전하지 않음.
            /*val todolist = vm.getList()
            for(i in 0 .. todolist.size-1){
                if(todolist[i].localTime!=null){
                //.truncatedTo(ChronoUnit.MINUTES)를 통한 분 이후의 값 제거
                    if(todolist[i].localTime.isAfter(LocalTime.now().truncatedTo(ChronoUnit.MINUTES))){
                        setAlarm(todolist[i])
                    }
                }
            }*/

            //알람삭제.. 구현실패
            //cancelAlarm(checkAlarm())
            //checkAlarm은 앱 실행시점기준 가장 근미래의 todo를 가져옴
            setAlarm(checkAlarm()) //임시코드, 가까운 todo 재실행시 잡아줌.
        }, 10000)

    }

    override fun onBackPressed() {
        if (fragStack.empty()) {super.onBackPressed()}
        else { exitFragment() }
    }

    override fun onNavigateUp(): Boolean {
        val navcon = binding.frgNav.getFragment<NavHostFragment>().navController

        return navcon.navigateUp() || super.onNavigateUp()
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
    //알람 울릴떄마다 삭제하고 알람 생성할떄 호출해서 써먹으려하였지만 삭재가 안되여 사용 안할 예정.
    fun checkAlarm():Todo?{
        val todolist = vm.getTodayList()
        var num=1000
        if(todolist.size!=0){
            var near = 1000000000
            for(i:Int in 0..todolist.size-1){
                //null가능성 체크
                if(todolist[i].localTime!=null) {
                    val tmp=(todolist[i].localTime.hour*60+todolist[i].localTime.minute
                            -(LocalTime.now().hour*60+LocalTime.now().minute))
                    if(tmp<0)
                        continue
                    if (near>tmp) {
                        near=tmp
                        num=i
                    }
                }
            }
            if(near==1000000000)
                return null
            //제대로 된 근미래값 todo return 확인 완료
            return todolist[num]
        }
        return null
    }
    //알람 설정
    fun setAlarm(todoNear: Todo?){
        if(todoNear!=null) {
            //System.out.println(todoNear.localTime)
            //System.out.println(todoNear.title)

            //intent를 통한 todo값 보내주기
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.putExtra("title", todoNear.title)
            intent.putExtra("description",todoNear.description)
            intent.putExtra("url",todoNear.url)

            //알람시간이 현재 기준으로 얼마만큼 뒤에 있는지 계산
            val time = (todoNear.localTime.hour*60+todoNear.localTime.minute
                    -LocalTime.now().hour*60-LocalTime.now().minute)

            //바로 실행하지 않기에 pending, 삭재에 실패하였기에 고려하지 않고 랜덤 사용
            val pendingIntent = PendingIntent.getBroadcast(
                this, Random().nextInt(999999999), intent, PendingIntent.FLAG_IMMUTABLE
            )

            getSystemService(AlarmManager::class.java).setExact(
                //기기시간 기준으로 실행
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                (SystemClock.elapsedRealtime() + time*60*1000),
                pendingIntent
            )

            //cancelAlarm(todoNear)
        }
    }
    /*알람 삭제함수.. 실패
    fun cancelAlarm(todoNear: Todo?) {
        if(todoNear!=null) {
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.putExtra("title", todoNear.title)
            intent.putExtra("description",todoNear.description)
            intent.putExtra("url",todoNear.url)
            val pendingIntent = PendingIntent.getBroadcast(
            this,0,intent,PendingIntent.FLAG_IMMUTABLE)
            getSystemService(AlarmManager::class.java).cancel(pendingIntent)
        }
    }*/




    /* todo: make it be able to be used generally, 안씀.
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
    }*/
}
