package com.example.playlistmaker.ui.search.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.search.model.Track

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var tracksList: ArrayList<Track> = ArrayList()
    lateinit var itemClickListener: ((Track) -> Unit)
    lateinit var itemLongClickListener: ((Track) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return tracksList.size
    }

    fun setTracks(tracks: List<Track>) {
        tracksList.clear()
        tracksList.addAll(tracks)
        notifyDataSetChanged() // Уведомляем об изменениях
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracksList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(tracksList[position])
        }
        holder.itemView.setOnLongClickListener {
            itemLongClickListener.invoke(tracksList[position])
            true
        }
    }
}