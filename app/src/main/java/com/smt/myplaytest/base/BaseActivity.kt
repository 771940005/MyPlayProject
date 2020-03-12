package com.smt.myplaytest.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 *@author hjy
 *Description: 所有activity的基类
 */
abstract class BaseActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initListener()
        initData()
        debug { "123" }
    }

    // 初始化数据
    open protected fun initData() {

    }

    // 控件布局
    open protected fun initListener() {

    }

    // 获取布局id
    abstract fun getLayoutId(): Int

    private fun myToast(msg: String) {
        runOnUiThread { toast(msg) }
    }

    // 开启activity并且finish当前界面
    inline fun <reified T : BaseActivity> startActivityAndFinish() {
        startActivity<T>()
        finish()
    }
}