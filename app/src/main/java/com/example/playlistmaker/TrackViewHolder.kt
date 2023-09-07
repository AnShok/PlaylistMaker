package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

// Внутренний класс ViewHolder, содержащий элементы интерфейса для каждого элемента списка
class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
    .inflate(R.layout.item_track, parent, false)) {

    companion object {
        private const val IMAGE_ROUND_DP = 2
    }

    private val trackNameTextView: TextView = itemView.findViewById(R.id.trackNameTextView)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artistNameTextView)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.trackTimeTextView)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.artworkImageView)

    // Метод для привязки данных трека к элементам интерфейса
    fun bind(track: Track) {
        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        val timeInMillis = track.trackTimeMillis
        val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeInMillis)
        trackTimeTextView.text = formattedTime

        // Создание объекта Glide для загрузки изображения
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(IMAGE_ROUND_DP))
            .into(artworkImageView)

    }
}