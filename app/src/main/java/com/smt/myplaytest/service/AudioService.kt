package com.smt.myplaytest.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.smt.myplaytest.R
import com.smt.myplaytest.model.AudioBean
import com.smt.myplaytest.ui.activity.AudioPlayerActivity
import com.smt.myplaytest.ui.activity.MainActivity
import de.greenrobot.event.EventBus
import kotlin.random.Random

/**
 *描述:播放器service
 */
class AudioService : Service() {

    var list: ArrayList<AudioBean>? = null
    var position: Int = -2  // 正在播放的position
    var mediaPlayer: MediaPlayer? = null
    private val binder by lazy { AudioBinder() }

    val FROM_PRE = 1
    val FROM_NEXT = 2
    val FROM_STATE = 3
    val FROM_CONTENT = 4

    var manager: NotificationManager? = null

    companion object {
        const val MODE_ALL = 1 // 列表循环
        const val MODE_SINGLE = 2 // 单曲循环
        const val MODE_RANDOM = 3 // 随机播放
    }

    var mode = MODE_ALL // 播放模式

    val sp: SharedPreferences by lazy { getSharedPreferences("config", Context.MODE_PRIVATE) }
    override fun onCreate() {
        super.onCreate()
        // 获取播放模式
        mode = sp.getInt("mode", 1)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pos = intent?.getIntExtra("position", -1) ?: -1  // 想要播放的position
        if (pos != position) {
            // 想要播放的条目 和 正在播放条目 不是同一首
            position = pos

            // 获取集合以及position
            list = intent?.getParcelableArrayListExtra<AudioBean>("list")

            // 开始播放音乐
            binder.playItem()
        } else {
            // 主动通知界面更新
            binder.notifyUpdateUi()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }


    inner class AudioBinder : Binder(), Iservice, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

        /**
         * 播放当前位置的歌曲
         */
        override fun playPosition(p2: Int) {
            this@AudioService.position = p2
            playItem()
        }

        /**
         * 获取播放集合
         */
        override fun getPlayList(): List<AudioBean>? {
            return list
        }

        /**
         * 播放上一曲
         */
        override fun playPre() {
            list?.let {
                // 获取要播放歌曲position
                when (mode) {
                    MODE_RANDOM -> list?.let { position = Random.nextInt(it.size - 1) }
                    else -> {
                        if (position == 0) {
                            position = it.size - 1
                        } else {
                            position--
                        }
                    }
                }
                // playitem
                playItem()
            }
        }

        /**
         * 播放下一曲
         */
        override fun playNext() {
            list?.let {
                // 获取要播放歌曲position
                when (mode) {
                    MODE_RANDOM -> list?.let { position = Random.nextInt(it.size - 1) }
                    else -> position = (position + 1) % it.size
                }
                // playitem
                playItem()
            }
        }

        /**
         * 获取播放模式
         */
        override fun getPlayMode(): Int {
            return mode
        }

        /**
         * 修改播放模式
         */
        override fun updatePlayMode() {
            when (mode) {
                MODE_ALL -> mode = MODE_SINGLE
                MODE_SINGLE -> mode = MODE_RANDOM
                MODE_RANDOM -> mode = MODE_ALL
            }
            // 保存播放模式
            sp.edit().putInt("mode", mode).apply()
        }

        /**
         * 歌曲播放完成之后回调
         */
        override fun onCompletion(mp: MediaPlayer?) {
            // 自动播放下一曲
            autoPlayNext()
        }

        /**
         * 根据播放模式自动播放下一曲
         */
        private fun autoPlayNext() {
            when (mode) {
                // 列表循环
                MODE_ALL -> {
                    //                    // 如果是最后一曲，返回到第一曲
                    //                    if (position==list.size-1){
                    //                        position = 0
                    //                    }else{
                    //                        position ++
                    //                    }
                    list?.let { position = (position + 1) % it.size }
                }
                // 随机播放
                MODE_RANDOM -> list?.let { position = Random.nextInt(it.size) }
            }
            playItem()
        }

        /**
         * 跳转到当前进度播放
         */
        override fun seekTo(progress: Int) {
            mediaPlayer?.seekTo(progress)
        }

        /**
         * 获取当前的进度
         */
        override fun getProgress(): Int {
            return mediaPlayer?.currentPosition ?: 0
        }

        /**
         * 获取总进度
         */
        override fun getDuration(): Int {
            return mediaPlayer?.duration ?: 0
        }

        /**
         * 更新播放状态
         */
        override fun updatePlayState() {
            // 获取当前播放状态
            val isPlaying = isPlaying()

            // 切换播放状态
            isPlaying?.let {
                if (isPlaying) {
                    // 播放 -> 暂停
                    mediaPlayer?.pause()
                } else {
                    // 暂停 -> 播放
                    mediaPlayer?.start()
                }
            }

        }


        override fun isPlaying(): Boolean? {
            return mediaPlayer?.isPlaying
        }

        override fun onPrepared(mp: MediaPlayer?) {
            // 播放音乐
            mediaPlayer?.start()
            // 通知界面更新
            notifyUpdateUi()
            // 显示通知
            showNotification()
        }


        /**
         * 显示通知
         */
        private fun showNotification() {
            manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager?.notify(1, getNotification())
        }

        /**
         * 创建notification
         * Notification 3.0
         * Notification.Builder 3.0
         *
         */
        private fun getNotification(): Notification? {
            val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            val notification = NotificationCompat.Builder(this@AudioService)
                .setTicker("正在播放歌曲${list?.get(position)?.display_name}")
                .setSmallIcon(R.mipmap.ic_launcher)

                //自定义通知view
                .setCustomContentView(getRemoteViews())
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)//设置不能滑动删除通知
                .setContentIntent(getPendingIntent())//通知栏主体点击事件
                .build()
            return notification
        }

        /**
         * 创建通知自定义view
         */
        private fun getRemoteViews(): RemoteViews? {
            val remoteViews = RemoteViews(packageName, R.layout.notification)
            //修改标题和内容
            remoteViews.setTextViewText(R.id.title, list?.get(position)?.display_name)
            remoteViews.setTextViewText(R.id.artist, list?.get(position)?.artist)
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
            val intent = Intent(this@AudioService, MainActivity::class.java)//点击主体进入当前界面中
            intent.putExtra("from", FROM_NEXT)
            val pendingIntent =
                PendingIntent.getService(
                    this@AudioService,
                    2,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            return pendingIntent
        }

        /**
         * 播放暂停按钮点击事件
         */
        private fun getStatePendingIntent(): PendingIntent? {
            val intent = Intent(this@AudioService, MainActivity::class.java)//点击主体进入当前界面中
            intent.putExtra("from", FROM_STATE)
            val pendingIntent =
                PendingIntent.getService(
                    this@AudioService,
                    3,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            return pendingIntent
        }

        /**
         * 上一曲点击事件
         */
        private fun getPrePendingIntent(): PendingIntent? {
            val intent = Intent(this@AudioService, MainActivity::class.java)//点击主体进入当前界面中
            intent.putExtra("from", FROM_PRE)
            val pendingIntent =
                PendingIntent.getService(
                    this@AudioService,
                    4,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            return pendingIntent
        }

        /**
         * 通知栏主体点击事件
         */
        private fun getPendingIntent(): PendingIntent? {
            val intent = Intent(this@AudioService, AudioPlayerActivity::class.java)//点击主体进入当前界面中
            intent.putExtra("from", FROM_CONTENT)
            val pendingIntent =
                PendingIntent.getActivity(
                    this@AudioService,
                    1,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            return pendingIntent
        }


        /**
         * 通知界面更新
         */
        fun notifyUpdateUi() {
            // 发送端
            EventBus.getDefault().post(list?.get(position))
        }

        fun playItem() {
            // 如果mediaPlayer已经存在就先释放掉
            if (mediaPlayer != null) {
                mediaPlayer?.reset()
                mediaPlayer?.release()
                mediaPlayer = null
            }

            mediaPlayer = MediaPlayer()
            mediaPlayer?.let {
                it.setOnPreparedListener(this)
                it.setOnCompletionListener(this)
                it.setDataSource(list?.get(position)?.data)
                it.prepareAsync()
            }

        }
    }

}