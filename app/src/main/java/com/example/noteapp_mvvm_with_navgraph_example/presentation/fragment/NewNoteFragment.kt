package com.example.noteapp_mvvm_with_navgraph_example.presentation.fragment

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.noteapp_mvvm_with_navgraph_example.R
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import com.example.noteapp_mvvm_with_navgraph_example.data.viewmodel.NoteViewModel
import com.example.noteapp_mvvm_with_navgraph_example.databinding.CustomeTimeAndDatePickerDialogBinding
import com.example.noteapp_mvvm_with_navgraph_example.databinding.FragmentNewNoteBinding
import com.example.noteapp_mvvm_with_navgraph_example.presentation.base.BaseFragment
import com.example.noteapp_mvvm_with_navgraph_example.utils.*
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custome_time_and_date_picker_dialog.*
import java.util.*


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
            dateTime.text = "EEEE, dd-MMMM-yyyy, hh:mm:ss a".dateTimeFormat()
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

            setAlert.setOnClickListener{
                val dialog = Dialog(requireActivity())
                dialog.setCancelable(true)
                dialog.setContentView(CustomeTimeAndDatePickerDialogBinding.inflate(layoutInflater).root)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                dialog.saveButton.setOnClickListener {
                    _dateTime = getTime(dialog)

                    val setDateTime = _dateTime?.getDateTimeIntoLong(requireContext())

                    setDateTime?.logI("DATE_TIME")

                    alertTimeDate.text = setDateTime

                    setAlert.setImageResource(R.drawable.baseline_access_alarm_24)

                    dialog.dismiss()
                }

                dialog.cancelButton.setOnClickListener { dialog.dismiss() }
                dialog.show()
            }

        }
    }

    private fun getTime(dialog: Dialog): Long {
        val minute = dialog.timePicker.minute
        val hour = dialog.timePicker.hour
        val day = dialog.datePicker.dayOfMonth
        val month = dialog.datePicker.month
        val year = dialog.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    private fun saveNote() {
        val noteTitle = binding?.etNoteTitle?.text.toString().trim()
        val noteBody = binding?.etNoteBody?.text.toString().trim()
        val currentDateTime = "EEEE, dd-MMMM-yyyy, hh:mm:ss a".dateTimeFormat()

        if (noteTitle.isNotEmpty() || noteBody.isNotEmpty()) {
            val note = Note(0, noteTitle, noteBody, currentDateTime, currentDateTime, selectedColor, _dateTime, 0, 0)

            notesViewModel.addNote(note)

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
                // Validate and handle the selected menu item
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