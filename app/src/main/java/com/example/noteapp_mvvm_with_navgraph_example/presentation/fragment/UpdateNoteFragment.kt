package com.example.noteapp_mvvm_with_navgraph_example.presentation.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapp_mvvm_with_navgraph_example.Constants.timeDateFormat
import com.example.noteapp_mvvm_with_navgraph_example.R
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import com.example.noteapp_mvvm_with_navgraph_example.data.viewmodel.NoteViewModel
import com.example.noteapp_mvvm_with_navgraph_example.databinding.CustomeTimeAndDatePickerDialogBinding
import com.example.noteapp_mvvm_with_navgraph_example.databinding.FragmentUpdateNoteBinding
import com.example.noteapp_mvvm_with_navgraph_example.presentation.base.BaseFragment
import com.example.noteapp_mvvm_with_navgraph_example.remainder.cancelAlarm
import com.example.noteapp_mvvm_with_navgraph_example.remainder.updateAlarm
import com.example.noteapp_mvvm_with_navgraph_example.utils.*
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custome_time_and_date_picker_dialog.*
import kotlinx.android.synthetic.main.fragment_update_note.*


@AndroidEntryPoint
class UpdateNoteFragment : BaseFragment<FragmentUpdateNoteBinding>() {

    private val notesViewModel by activityViewModels<NoteViewModel>()

    private val args: UpdateNoteFragmentArgs by navArgs()
    private lateinit var currentNote: Note
    private var _dateTime: Long? = null


    override fun setBinding(): FragmentUpdateNoteBinding =
        FragmentUpdateNoteBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()

        if (args.note == null) {
            findNavController().popBackStack()
            return
        }
        args.note?.let { currentNote = it }

        binding?.apply {
            currentNote.time?.let {
                alertTimeDate.text = it.getDateTimeIntoLong(requireContext())
            }

            dateTime.text = currentNote.updatedAt
            etNoteBodyUpdate.setText(currentNote.noteBody)
            etNoteTitleUpdate.setText(currentNote.noteTitle)
            chooseColorMcvBtn.setCardBackgroundColor(currentNote.noteColor)

            chooseColorMcvBtn.setOnClickListener {

                ColorPickerDialog.Builder(activity)
                    .setTitle("ColorPicker Dialog")
                    .setPreferenceName("MyColorPickerDialog")
                    .setPositiveButton(getString(R.string.confirm),
                        ColorEnvelopeListener { envelope, fromUser ->
                            chooseColorMcvBtn.setCardBackgroundColor(envelope.color)
                            currentNote.noteColor = envelope.color
                        })
                    .setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
                    .attachAlphaSlideBar(true)
                    .attachBrightnessSlideBar(true)
                    .setBottomSpace(12)
                    .show()
            }

            updateAlertIconTint(currentNote.alertStatus)

            setAlert.setOnClickListener {
                val dialog = Dialog(requireActivity())
                dialog.setCancelable(true)
                dialog.setContentView(CustomeTimeAndDatePickerDialogBinding.inflate(layoutInflater).root)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                dialog.saveButton.setOnClickListener {
                    _dateTime = getTime(dialog)
                    val setDateTime = _dateTime?.getDateTimeIntoLong(requireContext())
                    setDateTime?.logI("DATE_TIME")
                    alertTimeDate.text = setDateTime
                    currentNote.alertStatus = 1
                    updateAlertIconTint(currentNote.alertStatus)
                    dialog.dismiss()
                }

                dialog.cancelButton.setOnClickListener { dialog.dismiss() }
                dialog.show()
            }

        }

        binding?.fabDone?.setOnClickListener {
            val title = binding?.etNoteTitleUpdate?.text.toString().trim()
            val body = binding?.etNoteBodyUpdate?.text.toString().trim()
            val updateDateTime = timeDateFormat.dateTimeFormat()

            if (title.isNotEmpty() || body.isNotEmpty()) {
                currentNote.noteTitle = title
                currentNote.noteBody = body
                currentNote.updatedAt = updateDateTime
                _dateTime?.let {
                    currentNote.time = it
                    updateAlarm(requireContext(), it, currentNote.noteTitle, currentNote.noteBody, currentNote.requestCode)
                }
                notesViewModel.updateNote(currentNote)

                findNavController().popBackStack()
            } else {
                activity?.toast("Field is empty!")
            }
        }
    }

    private fun updateAlertIconTint(alertStatus: Int) {
        when (alertStatus) {
            0 -> {
                setAlert.apply {
                    setImageResource(R.drawable.baseline_add_alarm_24)
                    imageTintList = resources.getColorStateList(R.color.black, null)
                }
            }
            1 -> {
                setAlert.apply {
                    setImageResource(R.drawable.ic_alarm_set)
                    imageTintList = resources.getColorStateList(R.color.green, null)
                }
            }
            2 -> {
                setAlert.apply {
                    setImageResource(R.drawable.ic_alarm_set)
                    imageTintList = resources.getColorStateList(R.color.orange, null)
                }
            }
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Are you sure you want to permanently delete this note?")
            setPositiveButton("DELETE") { _, _ ->
                cancelAlarm(requireContext(), currentNote.requestCode)
                notesViewModel.deleteNote(currentNote)
                findNavController().popBackStack()
            }
            setNegativeButton("CANCEL", null)
        }.create().show()

    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_update_note, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_delete -> {
                        deleteNote()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}