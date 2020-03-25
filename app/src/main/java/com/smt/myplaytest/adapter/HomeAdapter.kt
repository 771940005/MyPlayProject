package com.smt.myplaytest.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smt.myplaytest.model.HomeItemBean
import com.smt.myplaytest.widget.HomeItemView

/**
 *@author hjy
 *Description:
 */
class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeHolder>() {

    private var list = ArrayList<HomeItemBean>()


    // 更新数据
    fun updateList(list: List<HomeItemBean>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }



    override fun onBindViewHolder(holder: HomeHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        return HomeHolder(HomeItemView(parent.context))
    }

    class HomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}