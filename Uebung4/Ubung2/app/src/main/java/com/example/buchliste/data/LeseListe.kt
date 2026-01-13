package com.example.buchliste.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leseliste")
data class LeseListe(
    @PrimaryKey(autoGenerate = true)
    val list_id: Int = 0,
    val name: String
)