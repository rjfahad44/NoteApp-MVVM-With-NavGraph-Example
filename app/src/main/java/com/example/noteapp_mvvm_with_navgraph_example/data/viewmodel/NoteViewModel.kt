package com.example.noteapp_mvvm_with_navgraph_example.data.viewmodel


import androidx.lifecycle.*
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import com.example.noteapp_mvvm_with_navgraph_example.data.repo.NotesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val notesRepo: NotesRepo) : ViewModel() {
    fun allNotes() = notesRepo.notes

    private val _findNoteByReqCode = MutableSharedFlow<Note>()
    val findNoteByReqCode = _findNoteByReqCode.asSharedFlow()
    fun addNote(note: Note) = viewModelScope.launch {
        notesRepo.insertNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        notesRepo.deleteNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        notesRepo.updateNote(note)
    }

    fun searchNote(query: String?) = notesRepo.searchNote(query)
    suspend fun findNoteByRequestCode(requestCode: Int){
        //_findNoteByReqCode.emit(notesRepo.findNoteByRequestCode(requestCode))
    }

}