package com.smt.myplaytest.ui.fragment

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.AsyncTask
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


class VBangFragment : BaseFragment() {

//    val handler = @SuppressLint("HandlerLeak")
//    object : Handler() {
//        override fun handleMessage(msg: Message) {
//            msg.let {
//                val cursor = msg.obj as Cursor
//                // 打印cursor
//                CursorUtil.logCursor(cursor)
//            }
//        }
//    }


    override fun initView(): View? {
        return View.inflate(context, R.layout.fragment_vbang, null)
    }

    override fun initData() {


        if (ContextCompat.checkSelfPermission(
                Objects.requireNonNull(context)!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                Objects.requireNonNull(activity)!!,
                arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                200
            )
        }

        // 加载音乐列表数据
        val resolver = context?.contentResolver


        // 开启线程查询音乐数据
//        Thread(object : Runnable {
//            override fun run() {
//                val cursor = resolver?.query(
//                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                    arrayOf(
//                        MediaStore.Audio.Media.DATA,
//                        MediaStore.Audio.Media.SIZE,
//                        MediaStore.Audio.Media.DISPLAY_NAME,
//                        MediaStore.Audio.Media.ARTIST
//                    ), null, null, null
//                )
//                val msg = Message.obtain()
//                msg.obj = cursor
//                handler.sendMessage(msg)
//            }
//
//        }).start()
        AudioTask().execute(resolver)
    }


    /**
     * 音乐查询异步任务
     */
    @Suppress("DEPRECATION")
    class AudioTask : AsyncTask<ContentResolver, Void, Cursor>() {

        /**
         *  后台执行任务   新线程
         */
        override fun doInBackground(vararg params: ContentResolver?): Cursor? {
            return params[0]?.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.ARTIST
                ), null, null, null
            )
        }

        /**
         *  将后台任务结果回调到主线程中
         */
        override fun onPostExecute(result: Cursor?) {
            super.onPostExecute(result)
            // 打印cursor
            CursorUtil.logCursor(result)
        }

    }
}