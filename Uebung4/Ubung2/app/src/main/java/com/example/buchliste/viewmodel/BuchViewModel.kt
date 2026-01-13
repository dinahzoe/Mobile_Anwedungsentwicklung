package com.example.buchliste.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.buchliste.data.Buch
import com.example.buchliste.data.BuchDatabase
import com.example.buchliste.data.BuchRepository
import kotlinx.coroutines.launch

class BuchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BuchRepository
    val allBuecher: LiveData<List<Buch>>

    init {
        val buchDao = BuchDatabase.getDatabase(application).buchDao()
        repository = BuchRepository(buchDao)
        allBuecher = repository.allBuecher.asLiveData()
    }

    fun insertBuch(title: String, autor: String, jahr: Int, gelesen: Boolean, listId: Int) = viewModelScope.launch {
        val buch = Buch(
            title = title,
            autor = autor,
            jahr = jahr,
            gelesen = gelesen,
            list_id = listId
        )
        repository.insertBuch(buch)
    }

    fun updateBuch(buch: Buch) = viewModelScope.launch {
        repository.updateBuch(buch)
    }

    fun deleteBuch(buch: Buch) = viewModelScope.launch {
        repository.deleteBuch(buch)
    }

    fun getBuecherByListId(listId: Int): LiveData<List<Buch>> {
        return repository.getBuecherByListId(listId).asLiveData()
    }

    fun toggleGelesen(buch: Buch) = viewModelScope.launch {
        val updatedBuch = buch.copy(gelesen = !buch.gelesen)
        repository.updateBuch(updatedBuch)
    }
}