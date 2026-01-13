package com.example.buchliste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buchliste.adapter.LeseListeAdapter
import com.example.buchliste.viewmodel.LeseListeViewModel
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private val viewModel: LeseListeViewModel by viewModels()
    private lateinit var adapter: LeseListeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Views initialisieren
        val etListeName: TextInputEditText = findViewById(R.id.etListeName)
        val btnAddListe: Button = findViewById(R.id.btnAddListe)
        val rvLeseListen: RecyclerView = findViewById(R.id.rvLeseListen)

        // RecyclerView Setup
        adapter = LeseListeAdapter(
            onOpenClick = { listId ->
                // Öffnet BuchListeActivity
                val intent = Intent(this, BuchListeActivity::class.java)
                intent.putExtra("LIST_ID", listId)
                startActivity(intent)
            },
            onDeleteClick = { listId ->
                // Bestätigung vor dem Löschen
                showDeleteConfirmationDialog(listId)
            }
        )

        rvLeseListen.adapter = adapter
        rvLeseListen.layoutManager = LinearLayoutManager(this)

        // Observer für die Leselisten
        viewModel.allLeseListenMitBuecher.observe(this) { listen ->
            listen?.let { adapter.submitList(it) }
        }

        // Button Click: Neue Liste hinzufügen
        btnAddListe.setOnClickListener {
            val name = etListeName.text.toString().trim()
            if (name.isNotEmpty()) {
                viewModel.insertLeseListe(name)
                etListeName.text?.clear()
                Toast.makeText(this, "Leseliste erstellt", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Bitte Namen eingeben", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(listId: Int) {
        AlertDialog.Builder(this)
            .setTitle("Leseliste löschen")
            .setMessage("Möchten Sie diese Leseliste wirklich löschen? Alle Bücher werden ebenfalls gelöscht.")
            .setPositiveButton("Löschen") { _, _ ->
                viewModel.allLeseListenMitBuecher.value?.find {
                    it.leseListe.list_id == listId
                }?.let { leseListeMitBuecher ->
                    viewModel.deleteLeseListe(leseListeMitBuecher.leseListe)
                    Toast.makeText(this, "Leseliste gelöscht", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Abbrechen", null)
            .show()
    }
}