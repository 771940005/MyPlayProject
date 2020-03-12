package com.smt.myplaytest.ui.activity

import android.widget.Toolbar
import com.smt.myplaytest.R
import com.smt.myplaytest.base.BaseActivity
import org.jetbrains.anko.find

class MainActivity : BaseActivity() {

    //惰性加载
//    override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {

    }


    override fun initListener() {


    }
}
