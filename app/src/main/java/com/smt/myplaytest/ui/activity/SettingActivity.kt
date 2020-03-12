package com.smt.myplaytest.ui.activity

import android.preference.PreferenceManager
import androidx.appcompat.widget.Toolbar
import com.smt.myplaytest.R
import com.smt.myplaytest.base.BaseActivity
import com.smt.myplaytest.util.ToolBarManager

/**
 *@author hjy
 *Description:
 */
@Suppress("DEPRECATION")
class SettingActivity : BaseActivity(), ToolBarManager {

    override val toobar by lazy { findViewById<Toolbar>(R.id.toolbar) }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initData() {
        initSettingTooBar()

        // 获取推送通知有没有选中
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val push = sp.getBoolean("push", false)
        println("push=${push}")
    }
}