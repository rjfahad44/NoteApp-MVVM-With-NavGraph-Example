package com.example.noteapp_mvvm_with_navgraph_example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import com.example.noteapp_mvvm_with_navgraph_example.data.local.dao.NoteDao

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NotesDataBase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}
