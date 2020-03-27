package com.smt.myplaytest.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.smt.myplaytest.model.AudioBean

/**
 *描述:播放器service
 */
class AudioService : Service() {

    var list: ArrayList<AudioBean>? = null
    var position: Int = 0
    var mediaPlayer: MediaPlayer? = null
    private val binder by lazy { AudioBinder() }

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

    inner class AudioBinder : Binder(), Iservice, MediaPlayer.OnPreparedListener {

        override fun onPrepared(mp: MediaPlayer?) {
            mediaPlayer?.start()
        }

        fun playItem() {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.let {
                it.setOnPreparedListener(this)
                it.setDataSource(list?.get(position)?.data)
                it.prepareAsync()
            }

        }
    }
}