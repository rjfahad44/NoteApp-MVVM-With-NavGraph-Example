package com.example.noteapp_mvvm_with_navgraph_example.data.repo

import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.example.noteapp_mvvm_with_navgraph_example.data.local.dao.NoteDao
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class NotesRepo @Inject constructor(private val notesDao: NoteDao) {

    val notes = notesDao.getAllNotes()

    suspend fun insertNote(note: Note) = notesDao.insertNote(note)
    suspend fun deleteNote(note: Note) = notesDao.deleteNote(note)
    suspend fun updateNote(note: Note) = notesDao.updateNote(note)
    fun searchNote(query: String?) = notesDao.searchNote(query)
}