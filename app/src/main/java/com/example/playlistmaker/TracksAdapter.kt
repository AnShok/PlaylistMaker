package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class TracksAdapter() : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    // Создание нового ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)


    // Привязка данных к ViewHolder и отображение элемента списка
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks.get(position))
    }

    // Возвращает общее количество элементов в списке
    override fun getItemCount(): Int =tracks.size
}