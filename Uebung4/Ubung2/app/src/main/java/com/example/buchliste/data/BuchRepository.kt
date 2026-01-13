package com.example.buchliste.data

import kotlinx.coroutines.flow.Flow

class BuchRepository(private val buchDao: BuchDao) {

    // LeseListe Operationen
    val allLeseListen: Flow<List<LeseListe>> = buchDao.getAllLeseListen()

    suspend fun insertLeseListe(leseListe: LeseListe): Long {
        return buchDao.insertLeseListe(leseListe)
    }

    suspend fun updateLeseListe(leseListe: LeseListe) {
        buchDao.updateLeseListe(leseListe)
    }

    suspend fun deleteLeseListe(leseListe: LeseListe) {
        buchDao.deleteLeseListe(leseListe)
    }

    fun getLeseListeById(id: Int): Flow<LeseListe?> {
        return buchDao.getLeseListeById(id)
    }

    // Buch Operationen
    val allBuecher: Flow<List<Buch>> = buchDao.getAllBuecher()

    suspend fun insertBuch(buch: Buch) {
        buchDao.insertBuch(buch)
    }

    suspend fun updateBuch(buch: Buch) {
        buchDao.updateBuch(buch)
    }

    suspend fun deleteBuch(buch: Buch) {
        buchDao.deleteBuch(buch)
    }

    fun getBuecherByListId(listId: Int): Flow<List<Buch>> {
        return buchDao.getBuecherByListId(listId)
    }

    // Verknüpfte Abfragen
    fun getLeseListeMitBuecher(listId: Int): Flow<LeseListeMitBuecher?> {
        return buchDao.getLeseListeMitBuecher(listId)
    }

    fun getAllLeseListenMitBuecher(): Flow<List<LeseListeMitBuecher>> {
        return buchDao.getAllLeseListenMitBuecher()
    }
}