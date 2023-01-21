package com.example.noteapp_mvvm_with_navgraph_example.presentation.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapp_mvvm_with_navgraph_example.R
import com.example.noteapp_mvvm_with_navgraph_example.data.adapter.NoteAdapter
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import com.example.noteapp_mvvm_with_navgraph_example.data.viewmodel.NoteViewModel
import com.example.noteapp_mvvm_with_navgraph_example.databinding.FragmentUpdateNoteBinding
import com.example.noteapp_mvvm_with_navgraph_example.presentation.base.BaseFragment
import com.example.noteapp_mvvm_with_navgraph_example.utils.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UpdateNoteFragment : BaseFragment<FragmentUpdateNoteBinding>() {

    private val notesViewModel by activityViewModels<NoteViewModel>()

    private val args: UpdateNoteFragmentArgs by navArgs()
    private lateinit var currentNote: Note

    private var selectedColor = 0

    override fun setBinding(): FragmentUpdateNoteBinding =
        FragmentUpdateNoteBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UpdateNoteFragmentArgs

        currentNote = args.note!!

        binding?.apply {

            etNoteBodyUpdate.setText(currentNote.noteBody)
            etNoteTitleUpdate.setText(currentNote.noteTitle)
            chooseColorMcvBtn.setCardBackgroundColor(currentNote.noteColor)

            /*chooseColorMcvBtn.setOnClickListener {
                ColorPickerPopup.Builder(activity).initialColor(selectedColor)
                    .enableBrightness(true)
                    .enableAlpha(true)
                    .okTitle("Choose")
                    .cancelTitle("Cancel")
                    .showIndicator(true)
                    .showValue(true)
                    .build()
                    .show(it, object : ColorPickerPopup.ColorPickerObserver() {
                        override fun onColorPicked(color: Int) {

                            selectedColor = color
                            Log.d("COLOR", "Color : $selectedColor")

                            chooseColorMcvBtn.setCardBackgroundColor(color)
                            cardView.setCardBackgroundColor(color)
                        }
                    })
            }*/

        }

        binding?.fabDone?.setOnClickListener {
            val title = binding?.etNoteTitleUpdate?.text.toString().trim()
            val body = binding?.etNoteBodyUpdate?.text.toString().trim()

            if (title.isNotEmpty()) {
                val note = Note(currentNote.id, title, body, selectedColor)
                notesViewModel.updateNote(note)

                findNavController().popBackStack()

            } else {
                activity?.toast("Enter a note title please")
            }
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Are you sure you want to permanently delete this note?")
            setPositiveButton("DELETE") { _, _ ->
                notesViewModel.deleteNote(currentNote)

                findNavController().popBackStack()
            }
            setNegativeButton("CANCEL", null)
        }.create().show()

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_update_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteNote()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}