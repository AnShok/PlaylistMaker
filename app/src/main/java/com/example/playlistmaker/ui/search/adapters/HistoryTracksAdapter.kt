package com.example.playlistmaker.ui.search.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.search.model.Track


class HistoryTracksAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var historyTracks = ArrayList<Track>()
    private var itemClickListener: SearchTracksAdapter.OnItemClickListener? = null

    interface OnItemClickListener : SearchTracksAdapter.OnItemClickListener {
        override fun onItemClick(track: Track)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    // Создание нового ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)


    // Привязка данных к ViewHolder и отображение элемента списка
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = historyTracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(track)
        }
    }

    // Возвращает общее количество элементов в списке
    override fun getItemCount(): Int = historyTracks.size
}