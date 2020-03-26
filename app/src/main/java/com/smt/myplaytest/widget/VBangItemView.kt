package com.smt.myplaytest.widget

import android.content.Context
import android.text.format.Formatter
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.smt.myplaytest.R
import com.smt.myplaytest.model.AudioBean
import kotlinx.android.synthetic.main.item_vbang.view.*

/**
 * 描述:
 */
class VBangItemView : RelativeLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.item_vbang,this)
    }

    /**
     * view+data绑定
     */
    fun setData(itemBean: AudioBean){
        // 歌曲名称
        title.text = itemBean.display_name
        // 歌手名
        artist.text = itemBean.artist
        // 歌曲大小   Formatter.formatFileSize: 转换以MB显示
        size.text = Formatter.formatFileSize(context,itemBean.size)
    }
}
