package com.example.noteapp_mvvm_with_navgraph_example.presentation.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.noteapp_mvvm_with_navgraph_example.R
import com.example.noteapp_mvvm_with_navgraph_example.data.adapter.NoteAdapter
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import com.example.noteapp_mvvm_with_navgraph_example.data.viewmodel.NoteViewModel
import com.example.noteapp_mvvm_with_navgraph_example.databinding.FragmentNewNoteBinding
import com.example.noteapp_mvvm_with_navgraph_example.presentation.base.BaseFragment
import com.example.noteapp_mvvm_with_navgraph_example.utils.snackBar
import com.example.noteapp_mvvm_with_navgraph_example.utils.toast
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewNoteFragment : BaseFragment<FragmentNewNoteBinding>() {

    private val notesViewModel by activityViewModels<NoteViewModel>()

    private var selectedColor = Color.GREEN

    override fun setBinding(): FragmentNewNoteBinding =
        FragmentNewNoteBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            chooseColorMcvBtn.setCardBackgroundColor(selectedColor)
            chooseColorMcvBtn.setOnClickListener {

//                ColorPickerPopup.Builder(activity).initialColor(selectedColor)
//                    .enableBrightness(true)
//                    .enableAlpha(true)
//                    .okTitle("Choose")
//                    .cancelTitle("Cancel")
//                    .showIndicator(true)
//                    .showValue(true)
//                    .build()
//                    .show(it, object : ColorPickerObserver() {
//                        override fun onColorPicked(color: Int) {
//
//                            selectedColor = color
//                            Log.d("COLOR", "Color : $selectedColor")
//
//                            chooseColorMcvBtn.setCardBackgroundColor(color)
//                            cardView.setCardBackgroundColor(color)
//                        }
//                    })
            }

        }
    }

    private fun saveNote() {
        val noteTitle = binding?.etNoteTitle?.text.toString().trim()
        val noteBody = binding?.etNoteBody?.text.toString().trim()

        if (noteTitle.isNotEmpty()) {
            val note = Note(0, noteTitle, noteBody, selectedColor)

            notesViewModel.addNote(note)

            findNavController().popBackStack()

            binding?.root?.let { "Note saved successfully".snackBar(it) }


        } else {
            "Please enter note title".toast(requireActivity())
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_new_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                saveNote()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}