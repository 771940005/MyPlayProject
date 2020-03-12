package com.smt.myplaytest.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast

/**
 *@author hjy
 *Description:所有Fragment的基类
 */
abstract class BaseFragment : Fragment(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    // fragment 的初始化
    private fun init() {

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initView()
    }

    // 获取布局view
    abstract fun initView(): View?

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListener()
        initData()
    }

    // 数据初始化
    private fun initData() {

    }

    // adapter listtener
    private fun initListener() {

    }

    fun myToast(msg: String) {
        context?.runOnUiThread { toast(msg) }
    }

}