package com.example.buchliste.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BuchDao {

    // LeseListe Operationen
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeseListe(leseListe: LeseListe): Long

    @Update
    suspend fun updateLeseListe(leseListe: LeseListe)

    @Delete
    suspend fun deleteLeseListe(leseListe: LeseListe)

    @Query("SELECT * FROM leseliste ORDER BY name ASC")
    fun getAllLeseListen(): Flow<List<LeseListe>>

    @Query("SELECT * FROM leseliste WHERE list_id = :id")
    fun getLeseListeById(id: Int): Flow<LeseListe?>

    // Buch Operationen
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuch(buch: Buch)

    @Update
    suspend fun updateBuch(buch: Buch)

    @Delete
    suspend fun deleteBuch(buch: Buch)

    @Query("SELECT * FROM buch WHERE list_id = :listId ORDER BY title ASC")
    fun getBuecherByListId(listId: Int): Flow<List<Buch>>

    @Query("SELECT * FROM buch ORDER BY title ASC")
    fun getAllBuecher(): Flow<List<Buch>>

    // Verknüpfte Abfrage
    @Transaction
    @Query("SELECT * FROM leseliste WHERE list_id = :listId")
    fun getLeseListeMitBuecher(listId: Int): Flow<LeseListeMitBuecher?>

    @Transaction
    @Query("SELECT * FROM leseliste ORDER BY name ASC")
    fun getAllLeseListenMitBuecher(): Flow<List<LeseListeMitBuecher>>
}