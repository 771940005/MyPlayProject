package com.smt.myplaytest.util

import com.smt.myplaytest.R
import com.smt.myplaytest.base.BaseFragment
import com.smt.myplaytest.ui.fragment.HomeFragment
import com.smt.myplaytest.ui.fragment.MvFragment
import com.smt.myplaytest.ui.fragment.VBangFragment
import com.smt.myplaytest.ui.fragment.YueDanFragment

/**
 *@author hjy
 *Description:
 * 管理fragment的util类
 */
class FragmentUtil private constructor() {
    // 设置单例  1.构造方法私有化 private constructor()  2.设置静态方法让其能引用
    val homeFragment by lazy { HomeFragment() }
    val mvFragment by lazy { MvFragment() }
    val vBangFragment by lazy { VBangFragment() }
    val yueDanFragment by lazy { YueDanFragment() }

    companion object {
        val fragmentUtil by lazy { FragmentUtil() }
    }

    /**
     * 根据tabId获取对应的fragment
     */
    fun getFragment(tabId: Int): BaseFragment? {

        when (tabId) {
            R.id.tab_home -> return homeFragment
            R.id.tab_mv -> return mvFragment
            R.id.tab_vbang -> return vBangFragment
            R.id.tab_yuedan -> return yueDanFragment
        }
        return null
    }
}