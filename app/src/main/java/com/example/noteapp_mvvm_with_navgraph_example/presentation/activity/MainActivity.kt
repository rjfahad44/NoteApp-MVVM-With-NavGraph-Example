package com.example.noteapp_mvvm_with_navgraph_example.presentation.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.noteapp_mvvm_with_navgraph_example.Constants
import com.example.noteapp_mvvm_with_navgraph_example.Constants.channelID
import com.example.noteapp_mvvm_with_navgraph_example.Constants.notificationId
import com.example.noteapp_mvvm_with_navgraph_example.data.viewmodel.NoteViewModel
import com.example.noteapp_mvvm_with_navgraph_example.databinding.ActivityMainBinding
import com.example.noteapp_mvvm_with_navgraph_example.presentation.base.BaseActivity
import com.example.noteapp_mvvm_with_navgraph_example.presentation.fragment.HomeFragmentDirections
import com.example.noteapp_mvvm_with_navgraph_example.utils.logI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val notesViewModel by viewModels<NoteViewModel>()

    override fun setBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onViewReady(savedInstanceState: Bundle?) {
        setSupportActionBar(binding.toolbar)

        createNotificationChannel(this)


        val requestCode = intent.getIntExtra(notificationId, -1)
        if (requestCode >= 1) {
            "call intent 1".logI("INTENT_CALL")
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(requestCode)
            lifecycleScope.launch {
                "call intent 2".logI("INTENT_CALL")
                notesViewModel.findNoteByRequestCode(requestCode).collectLatest { note ->
                    "call intent 3 $requestCode".logI("INTENT_CALL")
                    binding.fragmentHost.findNavController()
                        .navigate(HomeFragmentDirections.actionHomeFragmentToUpdateNoteFragment(note))
                }
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Note Alarm"
            val description = "Note Alert Notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelID, name, importance)
            channel.description = description

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}