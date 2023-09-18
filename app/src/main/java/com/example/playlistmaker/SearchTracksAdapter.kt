package com.example.playlistmaker

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class SearchTracksAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var searchTracks = ArrayList<Track>()
    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(track: Track)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    // Создание нового ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)


    // Привязка данных к ViewHolder и отображение элемента списка
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = searchTracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(track)
            Log.d("MyApp", "SearchTracksAdapter: Нажат трек $track")
        }
    }

    // Возвращает общее количество элементов в списке
    override fun getItemCount(): Int = searchTracks.size
}