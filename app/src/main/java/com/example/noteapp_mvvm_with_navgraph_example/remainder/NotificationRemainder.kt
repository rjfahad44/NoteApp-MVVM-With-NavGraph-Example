package com.example.noteapp_mvvm_with_navgraph_example.remainder

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.noteapp_mvvm_with_navgraph_example.Constants.channelID
import com.example.noteapp_mvvm_with_navgraph_example.Constants.messageExtra
import com.example.noteapp_mvvm_with_navgraph_example.Constants.notificationId
import com.example.noteapp_mvvm_with_navgraph_example.Constants.titleExtra
import com.example.noteapp_mvvm_with_navgraph_example.R
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import com.example.noteapp_mvvm_with_navgraph_example.data.repo.NotesRepo
import com.example.noteapp_mvvm_with_navgraph_example.presentation.activity.MainActivity
import com.example.noteapp_mvvm_with_navgraph_example.utils.logI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class NotificationRemainder : BroadcastReceiver() {

    @Inject
    lateinit var notesRepo: NotesRepo

    override fun onReceive(context: Context, intent: Intent) {
        val notificationID = intent.getIntExtra(notificationId, -1)

        var note: Note? = null

        CoroutineScope(Dispatchers.IO).launch {
            notesRepo.findNoteByRequestCode(notificationID).collectLatest {
                "Note $it".logI("NOTE_MODEL")
                it.alertStatus = 2
                note = it
                notesRepo.updateNote(it)
               pushNotificationWithNotify(context, intent, notificationID, it)
            }
        }

    }

    private fun pushNotificationWithNotify(
        context: Context,
        intent: Intent,
        notificationID: Int,
        note: Note
    ) {

        val notification = NotificationCompat.Builder(context, channelID).apply {
            setSmallIcon(R.drawable.ic_alarm_set)
            setContentTitle(intent.getStringExtra(titleExtra))
            setContentText(intent.getStringExtra(messageExtra))
            priority = NotificationCompat.PRIORITY_MAX
            setContentIntent(openActivityByPendingIntent(context, note))
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification.build())
    }

    private fun openActivityByPendingIntent(context: Context, note: Note): PendingIntent {
        /*val openActivityIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(notificationId, notificationID)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent
            .getActivity(
                context,
                notificationID,
                openActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )*/

        val args = Bundle()
        args.putParcelable("note", note)
        args.putBoolean("isAlert", true)
        return NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.updateNoteFragment)
            .setArguments(args)
            .createPendingIntent()
    }
}