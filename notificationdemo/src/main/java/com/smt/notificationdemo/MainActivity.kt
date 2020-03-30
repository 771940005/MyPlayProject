package com.smt.notificationdemo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {
    var manager: NotificationManager? = null
    var id = "channel_001"
    var name = "name"
    var notification :Notification?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val args = intent.getStringExtra("args")
        args?.let {
            Toast.makeText(this, args, Toast.LENGTH_SHORT).show()
        }
    }

    fun show(view: View) {
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        manager?.notify(1, getNotification())
        getNotification()
    }

    /**
     * 创建notification
     * Notification 3.0
     * Notification.Builder 3.0
     *
     */
    private fun getNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW)
            manager?.createNotificationChannel(mChannel)
             notification = Notification.Builder(this)
                .setChannelId(id)
                .setTicker("正在播放歌曲北京")
                .setContentTitle("北京")
                .setContentText("汪峰")
                .setSmallIcon(R.mipmap.ic_launcher)
                //自定义通知view
                .setCustomContentView(getRemoteViews())
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)//设置不能滑动删除通知
                .setContentIntent(getPendingIntent())//通知栏主体点击事件
                .build()

        }else{
            val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
             notification = NotificationCompat.Builder(this)
                .setTicker("正在播放歌曲北京")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bitmap)
                .setContentTitle("北京")//通知标题
                .setContentText("汪峰")//通知内容
                //自定义通知view
                .setCustomContentView(getRemoteViews())
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)//设置不能滑动删除通知
                .setContentIntent(getPendingIntent())//通知栏主体点击事件
                .build()

        }

        manager?.notify(1, notification)

    }

    /**
     * 创建通知自定义view
     */
    private fun getRemoteViews(): RemoteViews? {
        val remoteViews = RemoteViews(packageName, R.layout.notification)
        //修改标题和内容
        remoteViews.setTextViewText(R.id.title, "北京")
        remoteViews.setTextViewText(R.id.artist, "汪峰")
        //处理上一曲 下一曲  状态点击事件
        remoteViews.setOnClickPendingIntent(R.id.pre, getPrePendingIntent())
        remoteViews.setOnClickPendingIntent(R.id.state, getStatePendingIntent())
        remoteViews.setOnClickPendingIntent(R.id.next, getNextPendingIntent())
        return remoteViews
    }

    /**
     * 下一曲点击事件
     */
    private fun getNextPendingIntent(): PendingIntent? {
        val intent = Intent(this, MainActivity::class.java)//点击主体进入当前界面中
        intent.putExtra("args", "从通知栏下一曲点击进来")
        val pendingIntent =
            PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent
    }

    /**
     * 播放暂停按钮点击事件
     */
    private fun getStatePendingIntent(): PendingIntent? {
        val intent = Intent(this, MainActivity::class.java)//点击主体进入当前界面中
        intent.putExtra("args", "从通知栏播放和暂停点击进来")
        val pendingIntent =
            PendingIntent.getActivity(this, 3, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent
    }

    /**
     * 上一曲点击事件
     */
    private fun getPrePendingIntent(): PendingIntent? {
        val intent = Intent(this, MainActivity::class.java)//点击主体进入当前界面中
        intent.putExtra("args", "从通知栏上一曲点击进来")
        val pendingIntent =
            PendingIntent.getActivity(this, 4, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent
    }

    /**
     * 通知栏主体点击事件
     */
    private fun getPendingIntent(): PendingIntent? {
        val intent = Intent(this, MainActivity::class.java)//点击主体进入当前界面中
        intent.putExtra("args", "从通知栏主体点击进来")
        val pendingIntent =
            PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent
    }

    fun hide(view: View) {
        manager?.cancel(1)
    }
}
