package com.example.noteapp_mvvm_with_navgraph_example.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var noteTitle: String,
    var noteBody: String,
    var createdAt: String?,
    var updatedAt: String?,
    var noteColor: Int,
) : Parcelable
