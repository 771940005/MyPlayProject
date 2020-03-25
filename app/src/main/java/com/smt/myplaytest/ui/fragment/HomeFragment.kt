package com.smt.myplaytest.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smt.myplaytest.R
import com.smt.myplaytest.adapter.HomeAdapter
import com.smt.myplaytest.base.BaseFragment
import com.smt.myplaytest.model.HomeItemBean
import com.smt.myplaytest.util.URLProviderUtils
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import java.io.IOException

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

    override fun initData() {
        // 初始化数据
        loadDatas()
    }

    private fun loadDatas() {
        val path = URLProviderUtils.getHomeUrl(0, 20)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(path)
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 子线程中调用的
                myToast("获取数据失败")
            }

            override fun onResponse(call: Call, response: Response) {
                myToast("获取数据成功")
                val result = response.body()?.string()

                println("获取数据成功: $result")

            }
        })

    }


}