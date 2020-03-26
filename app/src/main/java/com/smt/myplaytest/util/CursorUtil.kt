package com.smt.myplaytest.util

import android.database.Cursor

/**
 * 描述:Cursor打印工具类
 */
object CursorUtil {

    fun logCursor(cursor: Cursor?) {
        cursor?.let {
            // cursor游标复位
            it.moveToPosition(-1)
            while (it.moveToNext()) {
                for (index in 0 until it.columnCount) {
                    println("key=${it.getColumnName(index)} --value=${it.getString(index)}")
                }
            }
        }
    }
}
