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
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    inner class AudioBinder : Binder(),Iservice, MediaPlayer.OnPreparedListener {

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