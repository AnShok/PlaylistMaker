package com.example.playlistmaker.ui.search.adapters

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

// Внутренний класс ViewHolder, содержащий элементы интерфейса для каждого элемента списка
class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackNameTextView: TextView = itemView.findViewById(R.id.track_name_text_view)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artist_name_text_view)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.track_time_text_view)
    private val artworkImageView: ImageView = itemView.findViewById(R.id.artwork_image_view)

    //Aункция для преобразования dp в px
    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    // Метод для привязки данных трека к элементам интерфейса
    fun bind(track: Track) {
        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        val timeInMillis = track.trackTimeMillis
        val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeInMillis)
        trackTimeTextView.text = formattedTime

        //Радиус в dp
        val cornerRadiusDp = 2f
        //Преобразование из dp в px
        val cornerRadiusPx = dpToPx(cornerRadiusDp, itemView.context)

        // Создание объекта Glide для загрузки изображения
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(cornerRadiusPx))
            .into(artworkImageView)
    }
    companion object {
        fun from(parent: ViewGroup): TrackViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_track, parent, false)
            return TrackViewHolder(view)
        }
    }
}