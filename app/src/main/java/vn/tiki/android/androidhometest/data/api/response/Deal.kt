package vn.tiki.android.androidhometest.data.api.response

import android.text.format.DateUtils
import java.text.DecimalFormat
import java.util.Date

class Deal(

        val productName: String,
        val productThumbnail: String,
        val productPrice: Double,
        val startedDate: Date,
        val endDate: Date
) {
    fun isExpired(now: Date): Boolean {
        return now.time > endDate.time
    }

    fun getTimeLeft(now: Date): String {
        if (endDate.time - now.time > 1000 * 60) {
            return DateUtils.getRelativeTimeSpanString(endDate.time, now.time, DateUtils.MINUTE_IN_MILLIS).toString()
        } else {
            return DateUtils.getRelativeTimeSpanString(endDate.time, now.time, DateUtils.SECOND_IN_MILLIS).toString()
        }
    }

    fun getPrice(): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(productPrice * 1000)
    }
}
