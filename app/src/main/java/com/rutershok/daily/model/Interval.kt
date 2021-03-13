package com.rutershok.daily.model

import android.content.Context
import com.rutershok.daily.R

enum class Interval(val nameRes: Int) {
    SECOND(R.string.second), MINUTE(R.string.minute), HOUR(R.string.hour), DAY(R.string.day),  //default
    WEEK(R.string.week), MONTH(R.string.month), NEVER(R.string.never);

    companion object {
        private val ARRAY = values()
        @JvmStatic
        var namesRes: IntArray? = null
            get() {
                if (field == null) {
                    field = IntArray(ARRAY.size)
                    for (i in field!!.indices) {
                        field!![i] = ARRAY[i].nameRes
                    }
                }
                return field
            }
            private set
        private var names: Array<String?>? = null

        @JvmStatic
        fun getNames(context: Context): Array<String?>? {
            if (names == null) {
                names = arrayOfNulls(ARRAY.size)
                for (i in ARRAY.indices) {
                    names!![i] = context.getString(ARRAY[i].nameRes)
                }
            }
            return names
        }
    }
}