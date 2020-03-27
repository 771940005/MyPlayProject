package com.smt.myplaytest.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.smt.myplaytest.model.AudioBean
import de.greenrobot.event.EventBus
import kotlin.random.Random

/**
 *描述:播放器service
 */
class AudioService : Service() {

    var list: ArrayList<AudioBean>? = null
    var position: Int = 0
    var mediaPlayer: MediaPlayer? = null
    private val binder by lazy { AudioBinder() }

    val MODE_ALL = 1 // 列表循环
    val MODE_SINGLE = 2 // 单曲循环
    val MODE_RANDOM = 3 // 随机播放
    var mode = MODE_ALL // 播放模式

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 获取集合以及position
        list = intent?.getParcelableArrayListExtra<AudioBean>("list")
        position = intent?.getIntExtra("position", -1) ?: -1

        // 开始播放音乐
        binder.playItem()

        // START_STICKY  粘性的 service强制杀死后 会尝试重新启动service  不会传递原来的intent(null)
        // START_NOT_STICKY 非粘性的 service强制杀死后  不会尝试重新启动service
        // START_REDELIVER_INTENT  粘性的 service强制杀死后 会尝试重新启动service  会传递原来的intent
        // START_REDELIVER_INTENT 太流氓  国产手机将其源码修改 作用和START_NOT_STICKY一样
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    inner class AudioBinder : Binder(), Iservice, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

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
        }

        /**
         * 通知界面更新
         */
        private fun notifyUpdateUi() {
            // 发送端
            EventBus.getDefault().post(list?.get(position))
        }

        fun playItem() {
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