package com.example.buchliste

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buchliste.adapter.BuchAdapter
import com.example.buchliste.viewmodel.BuchViewModel
import com.example.buchliste.viewmodel.LeseListeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import android.widget.TextView

class BuchListeActivity : AppCompatActivity() {

    private val buchViewModel: BuchViewModel by viewModels()
    private val leseListeViewModel: LeseListeViewModel by viewModels()
    private lateinit var adapter: BuchAdapter
    private var listId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buch_liste)

        // List ID aus Intent holen
        listId = intent.getIntExtra("LIST_ID", -1)
        if (listId == -1) {
            Toast.makeText(this, "Fehler: Keine Liste gefunden", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Views initialisieren
        val tvListeTitle: TextView = findViewById(R.id.tvListeTitle)
        val fabAddBuch: FloatingActionButton = findViewById(R.id.fabAddBuch)
        val rvBuecher: RecyclerView = findViewById(R.id.rvBuecher)

        // Listenname anzeigen
        leseListeViewModel.getLeseListeById(listId).observe(this) { leseListe ->
            leseListe?.let {
                tvListeTitle.text = it.name
            }
        }

        // RecyclerView Setup
        adapter = BuchAdapter(
            onDeleteClick = { buch ->
                buchViewModel.deleteBuch(buch)
                Toast.makeText(this, "Buch gelöscht", Toast.LENGTH_SHORT).show()
            },
            onGelesenClick = { buch ->
                buchViewModel.toggleGelesen(buch)
            }
        )

        rvBuecher.adapter = adapter
        rvBuecher.layoutManager = LinearLayoutManager(this)

        // Observer für Bücher
        buchViewModel.getBuecherByListId(listId).observe(this) { buecher ->
            buecher?.let { adapter.submitList(it) }
        }

        // FAB Click: Neues Buch hinzufügen
        fabAddBuch.setOnClickListener {
            showAddBuchDialog()
        }

        // Back Button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showAddBuchDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_buch, null)

        val etTitle = dialogView.findViewById<TextInputEditText>(R.id.etBuchTitle)
        val etAutor = dialogView.findViewById<TextInputEditText>(R.id.etBuchAutor)
        val etJahr = dialogView.findViewById<TextInputEditText>(R.id.etBuchJahr)
        val cbGelesen = dialogView.findViewById<CheckBox>(R.id.cbBuchGelesen)

        AlertDialog.Builder(this)
            .setTitle("Neues Buch hinzufügen")
            .setView(dialogView)
            .setPositiveButton("Hinzufügen") { _, _ ->
                val title = etTitle.text.toString().trim()
                val autor = etAutor.text.toString().trim()
                val jahrStr = etJahr.text.toString().trim()
                val gelesen = cbGelesen.isChecked

                if (title.isNotEmpty() && autor.isNotEmpty() && jahrStr.isNotEmpty()) {
                    val jahr = jahrStr.toIntOrNull() ?: 0
                    buchViewModel.insertBuch(title, autor, jahr, gelesen, listId)
                    Toast.makeText(this, "Buch hinzugefügt", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Abbrechen", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}