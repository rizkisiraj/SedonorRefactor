package com.example.sedonortdd.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ParseDate {

    fun parseDateTime(dateTimeStr: String): Calendar? {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        return try {
            val date = format.parse(dateTimeStr) // Parse String ke Date
            Calendar.getInstance().apply {
                time = date!!
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    fun scheduleNotificationAt(context: Context, year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1) // HATI-HATI: Calendar.MONTH itu 0-11
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

}