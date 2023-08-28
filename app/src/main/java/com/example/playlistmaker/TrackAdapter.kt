package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


class TrackAdapter(private val trackList: List<Track>) :
    RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    companion object {
        private const val IMAGE_ROUND_DP = 2
    }

    // Создание нового ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return ViewHolder(view)
    }

    // Привязка данных к ViewHolder и отображение элемента списка
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)
    }

    // Возвращает общее количество элементов в списке
    override fun getItemCount(): Int {
        return trackList.size
    }

    // Внутренний класс ViewHolder, содержащий элементы интерфейса для каждого элемента списка
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val trackNameTextView: TextView = itemView.findViewById(R.id.trackNameTextView)
        private val artistNameTextView: TextView = itemView.findViewById(R.id.artistNameTextView)
        private val trackTimeTextView: TextView = itemView.findViewById(R.id.trackTimeTextView)
        private val artworkImageView: ImageView = itemView.findViewById(R.id.artworkImageView)

        // Метод для привязки данных трека к элементам интерфейса
        fun bind(track: Track) {
            trackNameTextView.text = track.trackName
            artistNameTextView.text = track.artistName
            trackTimeTextView.text = track.trackTime

            // Создание объекта Glide для загрузки изображения
            Glide.with(itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(IMAGE_ROUND_DP))
                .into(artworkImageView)

        }
    }

}