package com.smt.myplaytest.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import com.smt.myplaytest.R
import com.smt.myplaytest.base.BaseActivity
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

        // 通过audioservice播放音乐
        val intent = intent
        intent.setClass(this, AudioService::class.java)

        // 开启服务
        startService(intent)

        // 绑定服务
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    var iService: Iservice? = null

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
            iService = service as Iservice
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        // 解绑服务
        unbindService(conn)
    }
}
