package com.example.buchliste.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buchliste.R
import com.example.buchliste.data.LeseListeMitBuecher

class LeseListeAdapter(
    private val onOpenClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<LeseListeAdapter.LeseListeViewHolder>() {

    private var leseListen = emptyList<LeseListeMitBuecher>()

    class LeseListeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvListeName: TextView = itemView.findViewById(R.id.tvListeName)
        val tvBuchAnzahl: TextView = itemView.findViewById(R.id.tvBuchAnzahl)
        val btnOpenListe: Button = itemView.findViewById(R.id.btnOpenListe)
        val btnDeleteListe: Button = itemView.findViewById(R.id.btnDeleteListe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeseListeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leseliste, parent, false)
        return LeseListeViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeseListeViewHolder, position: Int) {
        val current = leseListen[position]
        holder.tvListeName.text = current.leseListe.name
        holder.tvBuchAnzahl.text = "${current.buecher.size} Bücher"

        holder.btnOpenListe.setOnClickListener {
            onOpenClick(current.leseListe.list_id)
        }

        holder.btnDeleteListe.setOnClickListener {
            onDeleteClick(current.leseListe.list_id)
        }
    }

    override fun getItemCount() = leseListen.size

    fun submitList(list: List<LeseListeMitBuecher>) {
        leseListen = list
        notifyDataSetChanged()
    }
}