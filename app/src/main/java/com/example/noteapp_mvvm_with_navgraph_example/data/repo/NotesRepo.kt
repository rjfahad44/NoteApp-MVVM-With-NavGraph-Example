package com.example.noteapp_mvvm_with_navgraph_example.data.repo

import com.example.noteapp_mvvm_with_navgraph_example.data.local.dao.NoteDao
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import javax.inject.Inject
import javax.inject.Singleton

class NotesRepo @Inject constructor(private val notesDao: NoteDao) {

    val notes = notesDao.getAllNotes()
    suspend fun insertNote(note: Note) = notesDao.insertNote(note)
    suspend fun deleteNote(note: Note) = notesDao.deleteNote(note)
    suspend fun updateNote(note: Note) = notesDao.updateNote(note)
    fun searchNote(query: String?) = notesDao.searchNote(query)
    fun findNoteByRequestCode(requestCode: Int) = notesDao.findNoteByRequestCode(requestCode)
}