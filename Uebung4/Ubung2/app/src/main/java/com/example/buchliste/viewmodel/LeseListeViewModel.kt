package com.example.buchliste.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.buchliste.data.BuchDatabase
import com.example.buchliste.data.BuchRepository
import com.example.buchliste.data.LeseListe
import com.example.buchliste.data.LeseListeMitBuecher
import kotlinx.coroutines.launch

class LeseListeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BuchRepository
    val allLeseListen: LiveData<List<LeseListe>>
    val allLeseListenMitBuecher: LiveData<List<LeseListeMitBuecher>>

    init {
        val buchDao = BuchDatabase.getDatabase(application).buchDao()
        repository = BuchRepository(buchDao)
        allLeseListen = repository.allLeseListen.asLiveData()
        allLeseListenMitBuecher = repository.getAllLeseListenMitBuecher().asLiveData()
    }

    fun insertLeseListe(name: String) = viewModelScope.launch {
        val leseListe = LeseListe(name = name)
        repository.insertLeseListe(leseListe)
    }

    fun updateLeseListe(leseListe: LeseListe) = viewModelScope.launch {
        repository.updateLeseListe(leseListe)
    }

    fun deleteLeseListe(leseListe: LeseListe) = viewModelScope.launch {
        repository.deleteLeseListe(leseListe)
    }

    fun getLeseListeById(id: Int): LiveData<LeseListe?> {
        return repository.getLeseListeById(id).asLiveData()
    }
}