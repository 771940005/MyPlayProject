package com.smt.servicedemo

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * 描述:
 */
class MyService : Service() {

    override fun onCreate() {
        super.onCreate()
        println("onCreate")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        println("onBind")
        return MyBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        println("onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy")
    }


    fun sayHello() {
        println("hello from service")
    }

    inner class MyBinder : Binder(),IService {
        override fun callSayHello() {
            sayHello()
        }
    }
}
