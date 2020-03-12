package com.smt.myplaytest.util

import androidx.appcompat.widget.Toolbar
import com.smt.myplaytest.R


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
    }
}