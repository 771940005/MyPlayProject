package com.smt.myplaytest.service

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

}
