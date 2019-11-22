package com.example.cbc.the_hack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CardStackAdapter(
        private var poems: List<Poem> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_spot, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var circular = position % poems.size
        val poem = poems[circular]
        holder.title.text = poem.title
        holder.dynasty.text = poem.dynasty
        holder.author.text = poem.author
        holder.body.text = poem.body
    }

    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }

    fun setSpots(poems: List<Poem>) {
        this.poems = poems
    }

    fun getSpots(): List<Poem> {
        return poems
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var dynasty: TextView = view.findViewById(R.id.dynasty)
        var author: TextView = view.findViewById(R.id.author)
        var body: TextView = view.findViewById(R.id.body)
    }

}
