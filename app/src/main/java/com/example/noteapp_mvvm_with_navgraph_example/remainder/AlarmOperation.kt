package com.example.noteapp_mvvm_with_navgraph_example.remainder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.noteapp_mvvm_with_navgraph_example.Constants

fun setAlarm(
    context: Context,
    dateTime: Long,
    title: String,
    message: String,
    requestCode: Int
) {
    val intent = Intent(context, NotificationRemainder::class.java).apply {
        putExtra(Constants.titleExtra, title)
        putExtra(Constants.messageExtra, message)
        putExtra(Constants.notificationId, requestCode)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateTime, pendingIntent)
}

fun updateAlarm(
    context: Context,
    dateTime: Long,
    title: String,
    message: String,
    requestCode: Int
) {
    val intent = Intent(context, NotificationRemainder::class.java).apply {
        putExtra(Constants.titleExtra, title)
        putExtra(Constants.messageExtra, message)
        putExtra(Constants.notificationId, requestCode)
    }

    var pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
    pendingIntent.cancel()

    pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateTime, pendingIntent)
}

fun cancelAlarm(context: Context, requestCode: Int) {
    val intent = Intent(context, NotificationRemainder::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
    pendingIntent.cancel()
}