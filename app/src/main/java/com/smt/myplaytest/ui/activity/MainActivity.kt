package com.smt.myplaytest.ui.activity

import androidx.appcompat.widget.Toolbar
import com.smt.myplaytest.R
import com.smt.myplaytest.base.BaseActivity
import com.smt.myplaytest.util.ToolBarManager
import org.jetbrains.anko.find

class MainActivity : BaseActivity(), ToolBarManager {

    override var toobar: Toolbar
        get() = find<Toolbar>(R.id.toolbar)
        set(value) {}

    // 惰性加载
    //override var toolbar: Toolbar by lazy {find<Toolbar>(R.id.toolbar) }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {

    }

    override fun initListener() {
        initMainToolBar()

    }
}
