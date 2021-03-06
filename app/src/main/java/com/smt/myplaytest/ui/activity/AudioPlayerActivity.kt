package com.smt.myplaytest.ui.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.view.View
import android.widget.AdapterView
import android.widget.SeekBar
import com.smt.myplaytest.R
import com.smt.myplaytest.adapter.PopAdapter
import com.smt.myplaytest.base.BaseActivity
import com.smt.myplaytest.model.AudioBean
import com.smt.myplaytest.service.AudioService
import com.smt.myplaytest.service.Iservice
import com.smt.myplaytest.util.StringUtil
import com.smt.myplaytest.widget.PlayListPopWindow
import de.greenrobot.event.EventBus
import kotlinx.android.synthetic.main.activity_music_player_bottom.*
import kotlinx.android.synthetic.main.activity_music_player_middle.*
import kotlinx.android.synthetic.main.activity_music_player_top.*

/**
 * 描述:音乐播放器
 */
class AudioPlayerActivity : BaseActivity(), View.OnClickListener, SeekBar.OnSeekBarChangeListener,
    AdapterView.OnItemClickListener {


    private val conn by lazy { AudioConnection() }
    var iService: Iservice? = null
    private var audioBean: AudioBean? = null
    var drawable: AnimationDrawable? = null
    private var duration: Int = 0
    private var handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_PROGRESS -> startUpdateProgress()
            }
        }
    }
    val MSG_PROGRESS = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_audio
    }

    override fun initListener() {
        back.setOnClickListener { finish() }
        // 播放状态切换
        state.setOnClickListener(this)

        // 进度条变化监听
        progress_sk.setOnSeekBarChangeListener(this)

        // 播放模式点击事件
        mode.setOnClickListener(this)

        // 上一曲和下一曲
        pre.setOnClickListener(this)
        next.setOnClickListener(this)

        // 播放列表
        playlist.setOnClickListener(this)
    }

    /**
     * 弹出的播放列表条目点击事件
     */
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // 播放当前的歌曲
        iService?.playPosition(position)
    }


    /**
     * 进度改变回调
     * @param progress 改变之后的进度
     * @param fromUser true通过用户手指拖动改变进度  false通过代码方式改变进度
     */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        // 判断是否是用户操作
        if (!fromUser) return
        // 更新播放进度
        iService?.seekTo(progress)
        // 更新界面进度显示
        updateProgress(progress)
    }

    /**
     * 手指触摸seekbar回调
     */
    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    /**
     * 手指离开seekbar回调
     */
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.state -> updatePlayState()
            R.id.mode -> updatePlayMode()
            R.id.pre -> iService?.playPre()
            R.id.next -> iService?.playNext()
            R.id.playlist -> showPlayList()
        }
    }


    /**
     * 显示播放列表
     */
    private fun showPlayList() {

        val list = iService?.getPlayList()
        list?.let {

            // 创建adapter
            val adapter = PopAdapter(list)

            // 获取底部高度
            val bottomH = audio_player_bottom.height
            val popWindow = PlayListPopWindow(this, adapter, this, window)
            popWindow.showAsDropDown(audio_player_bottom, 0, -bottomH)
        }
    }


    /**
     * 更新播放模式
     */
    private fun updatePlayMode() {
        // 修改service中的mode
        iService?.updatePlayMode()

        // 修改界面模式图标
        updatePlayModeBtn()
    }


    /**
     * 根据播放模式修改播放模式图标
     */
    private fun updatePlayModeBtn() {
        iService?.let {
            // 获取播放模式
            val model: Int = it.getPlayMode()

            // 设置图标
            when (model) {
                AudioService.MODE_ALL -> mode.setImageResource(R.drawable.selector_btn_playmode_order)
                AudioService.MODE_SINGLE -> mode.setImageResource(R.drawable.selector_btn_playmode_single)
                AudioService.MODE_RANDOM -> mode.setImageResource(R.drawable.selector_btn_playmode_random)

            }
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
                // 开始播放动画
                drawable?.start()
                // 开始更新进度
                handler.sendEmptyMessage(MSG_PROGRESS)
            } else {
                // 暂停
                state.setImageResource(R.drawable.selector_btn_audio_pause)
                // 停止播放动画
                drawable?.stop()
                // 停止更新进度
                handler.removeMessages(MSG_PROGRESS)
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

        // 动画处理
        drawable = audio_anim.drawable as AnimationDrawable
        drawable?.start()

        // 获取总进度
        duration = iService?.getDuration() ?: 0

        // 进度条设置进度最大值
        progress_sk.max = duration

        // 更新播放进度
        startUpdateProgress()

        // 更新播放模式图标
        updatePlayModeBtn()

    }


    /**
     * 开始更新进度
     */
    private fun startUpdateProgress() {
        // 获取当前进度
        val progress: Int = iService?.getProgress() ?: 0

        // 更新进度数据
        updateProgress(progress)

        // 定时获取进度
        handler.sendEmptyMessageDelayed(MSG_PROGRESS, 1000)
    }


    /**
     * 根据当前进度数据更新界面
     */
    @SuppressLint("SetTextI18n")
    private fun updateProgress(pro: Int) {
        // 更新进度数值
        progress.text = StringUtil.parseDuration(pro) + "/" + StringUtil.parseDuration(duration)

        // 更新进度条
        progress_sk.progress = pro
    }


    override fun initData() {
        // 注册EvenBus
        EventBus.getDefault().register(this)

        // 通过audioservice播放音乐
        val intent = intent
        intent.setClass(this, AudioService::class.java)

        // 在这里一定要先绑定服务
        bindService(intent, conn, Context.BIND_AUTO_CREATE)

        // 再开启服务
        startService(intent)

    }


    /**
     * service连接
     */
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
        // 清空handler发送的所有消息
        handler.removeCallbacksAndMessages(null)
    }
}
