package com.example.noteapp_mvvm_with_navgraph_example.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM NOTES ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM NOTES WHERE noteTitle LIKE :query OR noteBody LIKE:query")
    fun searchNote(query: String?): LiveData<List<Note>>

    @Query("SELECT * FROM NOTES WHERE requestCode =:requestCode")
    fun findNoteByRequestCode(requestCode: Int): Flow<Note>
}
