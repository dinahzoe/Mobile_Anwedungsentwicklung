package com.example.buchliste.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "buch",
    foreignKeys = [
        ForeignKey(
            entity = LeseListe::class,
            parentColumns = ["list_id"],
            childColumns = ["list_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["list_id"])]
)
data class Buch(
    @PrimaryKey(autoGenerate = true)
    val buch_id: Int = 0,
    val title: String,
    val autor: String,
    val jahr: Int,
    val gelesen: Boolean = false,
    val list_id: Int
)