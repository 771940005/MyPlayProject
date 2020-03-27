package com.smt.myplaytest.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import android.view.View
import com.smt.myplaytest.R
import com.smt.myplaytest.base.BaseActivity
import com.smt.myplaytest.model.AudioBean
import com.smt.myplaytest.service.AudioService
import com.smt.myplaytest.service.Iservice
import de.greenrobot.event.EventBus
import kotlinx.android.synthetic.main.activity_music_player_bottom.*
import kotlinx.android.synthetic.main.activity_music_player_top.*

/**
 * 描述:
 */
class AudioPlayerActivity : BaseActivity(), View.OnClickListener {

    private val conn by lazy { AudioConnection() }

    var audioBean: AudioBean? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_audio
    }

    override fun initListener() {
        // 播放状态切换
        state.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.state -> updatePlayState()
        }

    }

    /**
     * 更新播放状态
     */
    private fun updatePlayState() {
        // 更新播放状态
        iService?.updatePlayState()

        // 更新播放状态图标
        updatePlayStateBtn()
    }

    /**
     * 根据播放状态 更新图标
     */
    private fun updatePlayStateBtn() {
        // 获取当前播放状态
        val isPlaying = iService?.isPlaying()

        // 根据状态更新图标
        isPlaying?.let {
            // 根据状态更新图标
            if (isPlaying) {
                // 播放
                state.setImageResource(R.drawable.selector_btn_audio_play)
            } else {
                // 暂停
                state.setImageResource(R.drawable.selector_btn_audio_pause)
            }
        }

    }

    /**
     * 接收eventbus
     */
    fun onEventMainThread(itemBean: AudioBean) {
        // 记录播放歌曲bean
        this.audioBean = itemBean

        // 歌曲名称
        audio_title.text = itemBean.display_name

        // 歌手名称
        artist.text = itemBean.artist

        // 更新播放状态按钮
        updatePlayStateBtn()
    }


    override fun initData() {
        // 注册EvenBus
        EventBus.getDefault().register(this)

        // 通过audioservice播放音乐
        val intent = intent
        intent.setClass(this, AudioService::class.java)

        // 开启服务
        startService(intent)

        // 绑定服务
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    var iService: Iservice? = null

    inner class AudioConnection : ServiceConnection {
        /**
         * 意外断开连接时
         */
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        /**
         * service连接时
         */
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iService = service as Iservice
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        // 解绑服务
        unbindService(conn)
        // 反注册
        EventBus.getDefault().unregister(this)
    }
}
