package com.smt.myplaytest.util

import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.smt.myplaytest.R
import com.smt.myplaytest.ui.activity.SettingActivity


/**
 *@author hjy
 *Description:
 * 所有界面toobar管理类
 */
interface ToolBarManager {

    val toobar: Toolbar

    // 初始化主界面中的toolbar
    fun initMainToolBar() {
        toobar.title = "我的影音"
        toobar.inflateMenu(R.menu.main)
        // kotlin 和 Java 调用特性
        // 如果Java接口中只有一个未实现的方法，可以省略接口对象，直接用{}表示未实现的方法
        toobar.setOnMenuItemClickListener {
            toobar.context.startActivity(Intent(toobar.context, SettingActivity::class.java))
            true
        }
//        toobar.setOnMenuItemClickListener { item ->
//            when (item?.itemId) {
//                // 跳转到设置界面
//                R.id.setting -> toobar.context.startActivity(
//                    Intent(
//                        toobar.context,
//                        SettingActivity::class.java
//                    )
//                )
//
//            }
//
//            true
//        }
    }


    fun initSettingTooBar() {
        toobar.title = "设置界面"
    }
}