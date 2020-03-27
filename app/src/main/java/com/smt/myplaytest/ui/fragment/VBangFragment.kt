package com.smt.myplaytest.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.AsyncQueryHandler
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import com.smt.myplaytest.R
import com.smt.myplaytest.adapter.VbangAdapter
import com.smt.myplaytest.base.BaseFragment
import com.smt.myplaytest.model.AudioBean
import com.smt.myplaytest.ui.activity.AudioPlayerActivity
import kotlinx.android.synthetic.main.fragment_vbang.*

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

        // 动态权限申请
        handlePermission()

        loadSongs()
    }

    /**
     * 处理权限问题
     */
    private fun handlePermission() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        //查看是否有权限
        val checkSelfPermission =
            context?.let { ActivityCompat.checkSelfPermission(it, permission) }
        if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
            //已经获取
            loadSongs()
        } else {
            //没有获取权限
            if (activity?.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        permission
                    )
                }!!) {
                //需要弹出
//                alert("我们只会访问音乐文件,不会访问隐私照片", "温馨提示") {
//                    yesButton { myRequestPermission() }
//                    noButton {}
//                }.show()
            } else {
                //不需要弹出
                myRequestPermission()
            }
        }
    }

    /**
     * 真正申请权限
     */
    private fun myRequestPermission() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        requestPermissions(permissions, 1)
    }

    /**
     * 接收权限授权结果
     * requestCode 请求码
     * permissions 权限申请数组
     * grantResults 申请之后结果
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadSongs()
        }
    }

    private fun loadSongs() {
        // 加载音乐列表数据
        val resolver = context?.contentResolver


        val handler = @SuppressLint("HandlerLeak")
        object : AsyncQueryHandler(resolver) {
            override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor) {
                // 查询完成回调  主线程
                // 打印cursor
                // CursorUtil.logCursor(cursor)

                // 刷新列表
                (cookie as? VbangAdapter)?.swapCursor(cursor)
            }
        }
        // 开始查询
        handler.startQuery(
            0, adapter, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST
            ), null, null, null
        )
    }

    var adapter: VbangAdapter? = null
    override fun initListener(view: View) {
        adapter = VbangAdapter(context, null)
        listView.adapter = adapter
        // 设置条目点击事件
        listView.setOnItemClickListener { parent, view, position, id ->
            // 获取数据集合
            val cursor = adapter?.getItem(position) as Cursor

            // 通过当前位置cursor获取整个播放列表
            val list: ArrayList<AudioBean> = AudioBean.getAudioBeans(cursor)

            // 跳转到音乐播放界面
            // startActivity<AudioPlayerActivity>()
            val intent = Intent(context, AudioPlayerActivity::class.java)
            intent.putExtra("list", list)
            intent.putExtra("position", position)
            startActivity(intent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        // 界面销毁  关闭cursor
        adapter?.changeCursor(null)
    }
}