package com.smt.myplaytest.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.smt.myplaytest.model.AudioBean
import com.smt.myplaytest.widget.PopListItemView

/**
 *描述:播放列表popwindow的适配器
 */
class PopAdapter(val list: List<AudioBean>) : BaseAdapter() {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView: PopListItemView? = null
        itemView = if (convertView == null) {
            PopListItemView(parent?.context)
        } else {
            convertView as PopListItemView
        }
        itemView.setData(list[position])
        return itemView
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}