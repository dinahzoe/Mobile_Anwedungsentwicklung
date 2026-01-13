package com.example.buchliste.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buchliste.R
import com.example.buchliste.data.Buch

class BuchAdapter(
    private val onDeleteClick: (Buch) -> Unit,
    private val onGelesenClick: (Buch) -> Unit
) : RecyclerView.Adapter<BuchAdapter.BuchViewHolder>() {

    private var buecher = emptyList<Buch>()

    class BuchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbGelesen: CheckBox = itemView.findViewById(R.id.cbGelesen)
        val tvTitle: TextView = itemView.findViewById(R.id.tvBuchTitle)
        val tvAutor: TextView = itemView.findViewById(R.id.tvBuchAutor)
        val tvJahr: TextView = itemView.findViewById(R.id.tvBuchJahr)
        val btnDelete: Button = itemView.findViewById(R.id.btnDeleteBuch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_buch, parent, false)
        return BuchViewHolder(view)
    }

    override fun onBindViewHolder(holder: BuchViewHolder, position: Int) {
        val current = buecher[position]

        holder.cbGelesen.isChecked = current.gelesen
        holder.tvTitle.text = current.title
        holder.tvAutor.text = "von ${current.autor}"
        holder.tvJahr.text = "Jahr: ${current.jahr}"

        // Durchgestrichen wenn gelesen
        if (current.gelesen) {
            holder.tvTitle.paintFlags = holder.tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.tvTitle.paintFlags = holder.tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.cbGelesen.setOnClickListener {
            onGelesenClick(current)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(current)
        }
    }

    override fun getItemCount() = buecher.size

    fun submitList(list: List<Buch>) {
        buecher = list
        notifyDataSetChanged()
    }
}