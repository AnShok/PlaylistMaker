package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class HistoryTracksAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var historyTracks = ArrayList<Track>()

    // Создание нового ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)


    // Привязка данных к ViewHolder и отображение элемента списка
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(historyTracks.get(position))
    }

    // Возвращает общее количество элементов в списке
    override fun getItemCount(): Int =historyTracks.size
}