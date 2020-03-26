package com.smt.myplaytest.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.IBinder
import com.smt.myplaytest.R
import com.smt.myplaytest.base.BaseActivity
import com.smt.myplaytest.model.AudioBean
import com.smt.myplaytest.service.AudioService
import com.smt.myplaytest.service.Iservice

/**
 * 描述:
 */
class AudioPlayerActivity : BaseActivity() {

    val conn by lazy { AudioConnection() }

    override fun getLayoutId(): Int {
        return R.layout.activity_audio
    }

    override fun initData() {

        val list = intent?.getParcelableArrayListExtra<AudioBean>("list")
        val position = intent?.getIntExtra("position", -1)
//        println("list=$list  position=$position")
//
//        // 播放音乐
//        val mediaPlayer = MediaPlayer()
//        mediaPlayer.setOnPreparedListener {
//            mediaPlayer.start()
//        }
//        mediaPlayer.setDataSource(list?.get(position ?: 0)?.data)
//        mediaPlayer.prepareAsync()

        // 通过audioservice播放音乐
        val intent = Intent(this, AudioService::class.java)

        // 通过intent将list以及position传递过去
        intent.putExtra("list", list)
        intent.putExtra("position", position)

        // 开启服务
        startService(intent)

        // 绑定服务
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

//    var iService: Iservice? = null

    inner class AudioConnection : ServiceConnection {
        /**
         * 意外断开连接时
         */
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        /**
         * service连接时
         */
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            iService = service as Iservice
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        // 解绑服务
        unbindService(conn)
    }
}
