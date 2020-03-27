package com.smt.myplaytest.service

import com.smt.myplaytest.model.AudioBean

/**
 * 描述:
 */
interface Iservice {

    fun updatePlayState()
    fun isPlaying(): Boolean?
    fun getDuration(): Int
    fun getProgress(): Int
    fun seekTo(progress: Int)
    fun updatePlayMode()
    fun getPlayMode(): Int
    fun playPre()
    fun playNext()
    fun getPlayList(): List<AudioBean>?
    fun playPosition(p2: Int)

}
