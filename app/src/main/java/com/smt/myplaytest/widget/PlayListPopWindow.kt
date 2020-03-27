package com.smt.myplaytest.widget

import android.content.Context
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.PopupWindow
import com.smt.myplaytest.R
import org.jetbrains.anko.find

/**
 *描述:PopupWindow 底部弹出框
 */
class PlayListPopWindow(context: Context, adapter: BaseAdapter,listener:AdapterView.OnItemClickListener) : PopupWindow() {
    init {
        // 设置布局
        val view = LayoutInflater.from(context).inflate(R.layout.pop_playlist, null, false)

        // 获取RecyclerView
        val listView = view.find<ListView>(R.id.listView)
        // 适配
        listView.adapter = adapter
        // 设置列表条目点击事件
        listView.setOnItemClickListener(listener)
        contentView = view

        // 设置宽度和高度
        width = ViewGroup.LayoutParams.MATCH_PARENT

        // 设置高度为屏幕高度的3/5
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        manager.defaultDisplay.getSize(point)
        val windowHeight = point.y
        height = (windowHeight * 3) / 5

        // 设置获取焦点
        isFocusable = true

        // 设置外部点击
        isOutsideTouchable = true

        // 响应返回按钮(低版本popwindow点击返回按钮能够dismiss关键)
        setBackgroundDrawable(ColorDrawable())

        // 处理popwindow动画
        animationStyle = R.style.pop
    }
}