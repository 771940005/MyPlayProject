package com.smt.myplaytest.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.smt.myplaytest.R
import com.smt.myplaytest.model.AudioBean
import kotlinx.android.synthetic.main.item_pop.view.*

/**
 *描述:
 */
class PopListItemView : RelativeLayout {
    fun setData(data: AudioBean) {
        // title
        title.text = data.display_name
        // artist
        artist.text = data.artist
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.item_pop, this)
    }
}