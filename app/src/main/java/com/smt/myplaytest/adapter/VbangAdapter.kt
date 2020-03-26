package com.smt.myplaytest.adapter

import android.content.Context
import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import com.smt.myplaytest.model.AudioBean
import com.smt.myplaytest.widget.VBangItemView

/**
 * 描述:vbang 列表适配器
 */
class VbangAdapter(context: Context?, c: Cursor?) : CursorAdapter(context, c) {

    /**
     * 创建条目view
     */
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return VBangItemView(context)
    }

    /**
     * view+data
     */
    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        // view
        val itemView = view as VBangItemView?

        // data
        val itemBean = AudioBean.getAudioBean(cursor)

        // view+data
        itemView?.setData(itemBean)
    }

}
