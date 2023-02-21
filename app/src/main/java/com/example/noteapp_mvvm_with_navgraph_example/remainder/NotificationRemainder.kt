package com.example.noteapp_mvvm_with_navgraph_example.remainder

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp_mvvm_with_navgraph_example.Constants.channelID
import com.example.noteapp_mvvm_with_navgraph_example.Constants.messageExtra
import com.example.noteapp_mvvm_with_navgraph_example.Constants.notificationId
import com.example.noteapp_mvvm_with_navgraph_example.Constants.titleExtra
import com.example.noteapp_mvvm_with_navgraph_example.R
import com.example.noteapp_mvvm_with_navgraph_example.data.viewmodel.NoteViewModel
import com.example.noteapp_mvvm_with_navgraph_example.presentation.activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class NotificationRemainder : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationID = intent.getIntExtra(notificationId, -1)
        pushNotificationWithNotify(context, intent, notificationID)
    }

    private fun pushNotificationWithNotify(context: Context, intent: Intent, notificationID: Int) {
        val notification = NotificationCompat.Builder(context, channelID).apply {
            setSmallIcon(R.drawable.ic_alarm_set)
            setContentTitle(intent.getStringExtra(titleExtra))
            setContentText(intent.getStringExtra(messageExtra))
            setContentIntent(openActivityByPendingIntent(context, notificationID))
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification.build())
    }

    private fun openActivityByPendingIntent(context: Context, notificationID: Int): PendingIntent {
        val openActivityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(notificationId, notificationID)
        }

        return PendingIntent
            .getActivity(
                context,
                notificationID,
                openActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
    }
}