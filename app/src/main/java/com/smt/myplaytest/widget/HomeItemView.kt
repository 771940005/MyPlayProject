package com.smt.myplaytest.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.smt.myplaytest.R

/**
 *@author hjy
 *Description:
 */

class HomeItemView : RelativeLayout {

    // 代码new相关
    constructor(context: Context?) : super(context)
    // 代码布局相关
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    // 代码主题相关
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /**
     * 初始化方法
     */
    init {
        // this:把当前的view添加到子view中,省略addView()操作
        View.inflate(context, R.layout.item_home, this)
    }
}