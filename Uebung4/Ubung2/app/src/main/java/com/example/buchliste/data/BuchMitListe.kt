package com.example.buchliste.data

import androidx.room.Embedded
import androidx.room.Relation

data class LeseListeMitBuecher(
    @Embedded val leseListe: LeseListe,
    @Relation(
        parentColumn = "list_id",
        entityColumn = "list_id"
    )
    val buecher: List<Buch>
)