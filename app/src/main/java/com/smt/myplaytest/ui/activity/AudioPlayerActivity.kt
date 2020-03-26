package com.smt.myplaytest.ui.activity

import android.media.MediaPlayer
import com.smt.myplaytest.R
import com.smt.myplaytest.base.BaseActivity
import com.smt.myplaytest.model.AudioBean

/**
 * 描述:
 */
class AudioPlayerActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_audio
    }

    override fun initData() {

        val list = intent?.getParcelableArrayListExtra<AudioBean>("list")
        val position = intent?.getIntExtra("position", -1)
        println("list=$list  position=$position")

        // 播放音乐
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
        mediaPlayer.setDataSource(list?.get(position ?: 0)?.data)
        mediaPlayer.prepareAsync()
    }


}
