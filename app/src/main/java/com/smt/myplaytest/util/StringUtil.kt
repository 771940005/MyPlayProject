package com.smt.myplaytest.util


/**
 * ClassName:StringUtil
 * Description:处理时间
 */
object StringUtil {

    val HOUR = 60 * 60 * 1000
    val MIN = 60 * 1000
    val SEC = 1000

    fun parseDuration(progress: Int): String {
        val hour = progress / HOUR
        val min = progress % HOUR / MIN
        val sec = progress % MIN / SEC
        val result: String

        result = if (hour == 0) {
            //不足1小时 不显示小时  %02d：显示两位数，不足以0补充
            String.format("%02d:%02d", min, sec)
        } else {
            String.format("%02d:%02d:%02d", hour, min, sec)
        }
        return result
    }
}