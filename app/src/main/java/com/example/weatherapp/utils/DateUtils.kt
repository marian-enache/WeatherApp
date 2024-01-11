package com.example.weatherapp.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private const val GENERAL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

    /**
    Date format: 2024-01-11 21:00:00
     */
    fun getDayFromDate(dateString: String): String? {
        val sdf = SimpleDateFormat(GENERAL_DATE_FORMAT, Locale.US)

        val date = try {
            sdf.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }
        val calendar = Calendar.getInstance()
        calendar.time = date

        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US)
    }

    fun getHourOfDay(dateString: String): Int {
        val sdf = SimpleDateFormat(GENERAL_DATE_FORMAT, Locale.US)

        val date = try {
            sdf.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            return -1
        }
        val calendar = Calendar.getInstance()
        calendar.time = date

        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun isSameDay(date1String: String, date2String: String): Boolean {
        val sdf = SimpleDateFormat(GENERAL_DATE_FORMAT, Locale.US)

        val date1 = try {
            sdf.parse(date1String)
        } catch (e: ParseException) {
            e.printStackTrace()
            return false
        }

        val date2 = try {
            sdf.parse(date2String)
        } catch (e: ParseException) {
            e.printStackTrace()
            return false
        }

        val calendar1 = Calendar.getInstance()
        calendar1.time = date1
        val calendar2 = Calendar.getInstance()
        calendar1.time = date2

        return calendar1.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US)
            ?.equals(calendar2.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US))
            ?: false
    }
}