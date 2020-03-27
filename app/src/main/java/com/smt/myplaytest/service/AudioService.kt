package com.smt.myplaytest.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.smt.myplaytest.model.AudioBean
import de.greenrobot.event.EventBus
import kotlin.random.Random

/**
 *描述:播放器service
 */
class AudioService : Service() {

    var list: ArrayList<AudioBean>? = null
    var position: Int = -2  // 正在播放的position
    var mediaPlayer: MediaPlayer? = null
    private val binder by lazy { AudioBinder() }

    companion object {
        const val MODE_ALL = 1 // 列表循环
        const val MODE_SINGLE = 2 // 单曲循环
        const val MODE_RANDOM = 3 // 随机播放
    }

    var mode = MODE_ALL // 播放模式

    val sp: SharedPreferences by lazy { getSharedPreferences("config", Context.MODE_PRIVATE) }
    override fun onCreate() {
        super.onCreate()
        // 获取播放模式
        mode = sp.getInt("mode", 1)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pos = intent?.getIntExtra("position", -1) ?: -1  // 想要播放的position
        if (pos != position) {
            // 想要播放的条目 和 正在播放条目 不是同一首
            position = pos

            // 获取集合以及position
            list = intent?.getParcelableArrayListExtra<AudioBean>("list")

            // 开始播放音乐
            binder.playItem()
        } else {
            // 主动通知界面更新
            binder.notifyUpdateUi()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    inner class AudioBinder : Binder(), Iservice, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

        /**
         * 播放当前位置的歌曲
         */
        override fun playPosition(p2: Int) {
            this@AudioService.position = p2
            playItem()
        }

        /**
         * 获取播放集合
         */
        override fun getPlayList(): List<AudioBean>? {
            return list
        }

        /**
         * 播放上一曲
         */
        override fun playPre() {
            list?.let {
                // 获取要播放歌曲position
                when (mode) {
                    MODE_RANDOM -> list?.let { position = Random.nextInt(it.size - 1) }
                    else -> {
                        if (position == 0) {
                            position = it.size - 1
                        } else {
                            position--
                        }
                    }
                }
                // playitem
                playItem()
            }
        }

        /**
         * 播放下一曲
         */
        override fun playNext() {
            list?.let {
                // 获取要播放歌曲position
                when (mode) {
                    MODE_RANDOM -> list?.let { position = Random.nextInt(it.size - 1) }
                    else -> position = (position + 1) % it.size
                }
                // playitem
                playItem()
            }
        }

        /**
         * 获取播放模式
         */
        override fun getPlayMode(): Int {
            return mode
        }

        /**
         * 修改播放模式
         */
        override fun updatePlayMode() {
            when (mode) {
                MODE_ALL -> mode = MODE_SINGLE
                MODE_SINGLE -> mode = MODE_RANDOM
                MODE_RANDOM -> mode = MODE_ALL
            }
            // 保存播放模式
            sp.edit().putInt("mode", mode).apply()
        }

        /**
         * 歌曲播放完成之后回调
         */
        override fun onCompletion(mp: MediaPlayer?) {
            // 自动播放下一曲
            autoPlayNext()
        }

        /**
         * 根据播放模式自动播放下一曲
         */
        private fun autoPlayNext() {
            when (mode) {
                // 列表循环
                MODE_ALL -> {
                    //                    // 如果是最后一曲，返回到第一曲
                    //                    if (position==list.size-1){
                    //                        position = 0
                    //                    }else{
                    //                        position ++
                    //                    }
                    list?.let { position = (position + 1) % it.size }
                }
                // 随机播放
                MODE_RANDOM -> list?.let { position = Random.nextInt(it.size) }
            }
            playItem()
        }

        /**
         * 跳转到当前进度播放
         */
        override fun seekTo(progress: Int) {
            mediaPlayer?.seekTo(progress)
        }

        /**
         * 获取当前的进度
         */
        override fun getProgress(): Int {
            return mediaPlayer?.currentPosition ?: 0
        }

        /**
         * 获取总进度
         */
        override fun getDuration(): Int {
            return mediaPlayer?.duration ?: 0
        }

        /**
         * 更新播放状态
         */
        override fun updatePlayState() {
            // 获取当前播放状态
            val isPlaying = isPlaying()

            // 切换播放状态
            isPlaying?.let {
                if (isPlaying) {
                    // 播放 -> 暂停
                    mediaPlayer?.pause()
                } else {
                    // 暂停 -> 播放
                    mediaPlayer?.start()
                }
            }

        }

        override fun isPlaying(): Boolean? {
            return mediaPlayer?.isPlaying
        }

        override fun onPrepared(mp: MediaPlayer?) {
            // 播放音乐
            mediaPlayer?.start()
            // 通知界面更新
            notifyUpdateUi()
        }

        /**
         * 通知界面更新
         */
        fun notifyUpdateUi() {
            // 发送端
            EventBus.getDefault().post(list?.get(position))
        }

        fun playItem() {
            // 如果mediaPlayer已经存在就先释放掉
            if (mediaPlayer != null) {
                mediaPlayer?.reset()
                mediaPlayer?.release()
                mediaPlayer = null
            }

            mediaPlayer = MediaPlayer()
            mediaPlayer?.let {
                it.setOnPreparedListener(this)
                it.setOnCompletionListener(this)
                it.setDataSource(list?.get(position)?.data)
                it.prepareAsync()
            }

        }
    }
}