package com.smt.myplaytest.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.smt.myplaytest.R
import com.smt.myplaytest.base.BaseFragment
import com.smt.myplaytest.util.CursorUtil
import java.util.*


/**
 *@author hjy
 *Description:
 */

@Suppress("DEPRECATION")
class VBangFragment : BaseFragment() {

    override fun initView(): View? {
        return View.inflate(context, R.layout.fragment_vbang, null)
    }

    override fun initData() {

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(context)!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                Objects.requireNonNull(activity)!!,
                arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                200
            )
        }

        // 加载音乐列表数据
        val resolver = context?.contentResolver
        val cursor = resolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST
            ), null, null, null
        )

        // 打印所有的数据
        if (cursor != null) {
            CursorUtil.logCursor(cursor)
        }
    }
}