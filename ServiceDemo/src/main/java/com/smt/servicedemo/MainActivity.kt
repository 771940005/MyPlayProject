package com.smt.servicedemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View

class MainActivity : AppCompatActivity() {

    var inten: Intent? = null
    val conn by lazy { MyConnection() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inten = Intent(this, MyService::class.java)
    }

    fun startService(v: View) {
        startService(inten)
    }

    fun stop(v: View) {
        stopService(inten)
    }

    fun bind(v: View) {
        bindService(inten, conn, Context.BIND_AUTO_CREATE)
    }

    fun invoke(v: View) {
        iService?.callSayHello()
    }

    fun unbind(v: View) {
        unbindService(conn)
    }

    var iService: IService? = null

    inner class MyConnection : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        /**
         * 服务连接上
         */
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iService = service as IService
        }

    }
}
