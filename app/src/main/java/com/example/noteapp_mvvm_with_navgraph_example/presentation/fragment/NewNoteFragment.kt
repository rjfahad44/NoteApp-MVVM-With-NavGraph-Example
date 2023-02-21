package com.example.noteapp_mvvm_with_navgraph_example.presentation.fragment

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.noteapp_mvvm_with_navgraph_example.Constants.timeDateFormat
import com.example.noteapp_mvvm_with_navgraph_example.R
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import com.example.noteapp_mvvm_with_navgraph_example.data.viewmodel.NoteViewModel
import com.example.noteapp_mvvm_with_navgraph_example.databinding.CustomeTimeAndDatePickerDialogBinding
import com.example.noteapp_mvvm_with_navgraph_example.databinding.FragmentNewNoteBinding
import com.example.noteapp_mvvm_with_navgraph_example.presentation.base.BaseFragment
import com.example.noteapp_mvvm_with_navgraph_example.remainder.setAlarm
import com.example.noteapp_mvvm_with_navgraph_example.utils.*
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custome_time_and_date_picker_dialog.*
import java.util.*
import kotlin.math.absoluteValue


@AndroidEntryPoint
class NewNoteFragment : BaseFragment<FragmentNewNoteBinding>() {

    private val notesViewModel by activityViewModels<NoteViewModel>()
    private var selectedColor = Color.WHITE
    private var _dateTime: Long? = null

    override fun setBinding(): FragmentNewNoteBinding =
        FragmentNewNoteBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()

        binding?.apply {
            dateTime.text = timeDateFormat.dateTimeFormat()
            chooseColorMcvBtn.setCardBackgroundColor(selectedColor)
            chooseColorMcvBtn.setOnClickListener {

                ColorPickerDialog.Builder(activity)
                    .setTitle("ColorPicker Dialog")
                    .setPreferenceName("MyColorPickerDialog")
                    .setPositiveButton(getString(R.string.confirm),
                        ColorEnvelopeListener { envelope, fromUser ->
                            chooseColorMcvBtn.setCardBackgroundColor(envelope.color)
                            selectedColor = envelope.color
                        })
                    .setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
                    .attachAlphaSlideBar(true)
                    .attachBrightnessSlideBar(true)
                    .setBottomSpace(12)
                    .show()
            }

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
                    setAlert.setImageResource(R.drawable.ic_alarm_set)
                    dialog.dismiss()
                }

                dialog.cancelButton.setOnClickListener { dialog.dismiss() }
                dialog.show()
            }

        }
    }

    private fun saveNote() {
        val noteTitle = binding?.etNoteTitle?.text.toString().trim()
        val noteBody = binding?.etNoteBody?.text.toString().trim()
        val currentDateTime = "EEEE, dd-MMMM-yyyy, hh:mm:ss a".dateTimeFormat()

        if (noteTitle.isNotEmpty() || noteBody.isNotEmpty()) {
            val requestCode = UUID.randomUUID().mostSignificantBits.absoluteValue.toInt()
            val note = Note(
                id = 0,
                noteTitle = noteTitle,
                noteBody = noteBody,
                createdAt = currentDateTime,
                updatedAt = currentDateTime,
                noteColor = selectedColor,
                time = _dateTime,
                requestCode = requestCode,
                alertColor = R.color.green,
                alertStatus = if (_dateTime !=null) 1 else 0
            )

            notesViewModel.addNote(note)

            if (_dateTime !=null){
                _dateTime?.let {
                    setAlarm(
                        requireContext(),
                        it,
                        noteTitle,
                        noteBody,
                        requestCode
                    )
                }
            }

            findNavController().popBackStack()
            binding?.root?.let { "Note saved successfully".snackBar(it) }
        } else {
            "Empty Note".toast(requireActivity())
        }
    }



    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_new_note, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_save -> {
                        saveNote()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}