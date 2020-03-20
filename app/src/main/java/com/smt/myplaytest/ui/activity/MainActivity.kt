package com.smt.myplaytest.ui.activity

import androidx.appcompat.widget.Toolbar
import com.smt.myplaytest.R
import com.smt.myplaytest.base.BaseActivity
import com.smt.myplaytest.util.FragmentUtil
import com.smt.myplaytest.util.ToolBarManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), ToolBarManager {

    // 惰性加载
    override val toobar: Toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        initMainToolBar()
    }

    override fun initListener() {

        // 设置tab切换监听
        bottomBar.setOnTabSelectListener {
            // it代表参数tabId
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(
                R.id.container,
                FragmentUtil.fragmentUtil.getFragment(it)!!,
                it.toString()
            )
            transaction.commit()
        }
    }
}
