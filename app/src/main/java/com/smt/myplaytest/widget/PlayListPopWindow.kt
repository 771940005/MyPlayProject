package com.smt.myplaytest.widget

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import com.smt.myplaytest.R

/**
 *描述:
 */
class PlayListPopWindow(context: Context) : PopupWindow() {
    init {
        // 设置布局
        val view = LayoutInflater.from(context).inflate(R.layout.pop_playlist, null, false)
        contentView = view

        // 设置宽度和高度
        width = ViewGroup.LayoutParams.MATCH_PARENT

        // 设置高度为屏幕高度的3/5
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        manager.defaultDisplay.getSize(point)
        val windowHeight = point.y
        height = (windowHeight * 3) / 5
    }
}