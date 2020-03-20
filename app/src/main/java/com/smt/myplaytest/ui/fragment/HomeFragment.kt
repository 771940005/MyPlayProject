package com.smt.myplaytest.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.smt.myplaytest.R
import com.smt.myplaytest.adapter.HomeAdapter
import com.smt.myplaytest.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*

/**
 *@author hjy
 *Description:
 */

class HomeFragment : BaseFragment() {
    override fun initView(): View? {
        return View.inflate(context, R.layout.fragment_home, null)
    }

    override fun initListener() {
        // 初始化recycleview
        homeRecyclerView.layoutManager = LinearLayoutManager(context)
        // 适配
        val adapter = HomeAdapter()
        homeRecyclerView.adapter = adapter

    }

}