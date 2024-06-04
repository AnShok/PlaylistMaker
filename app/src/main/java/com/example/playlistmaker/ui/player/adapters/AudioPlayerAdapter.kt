package com.example.playlistmaker.ui.player.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Playlist

class AudioPlayerAdapter(private val clickListener: AudioPlayerViewHolder.ClickListener) :
    RecyclerView.Adapter<AudioPlayerViewHolder>() {
    var playlists = ArrayList<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioPlayerViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_bottom_sheet_playlist, parent, false)
        return AudioPlayerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: AudioPlayerViewHolder, position: Int) {
        holder.bind(playlists[position], clickListener)
    }

    fun updatePlaylists(newPlaylists: List<Playlist>) {
        playlists.clear()
        playlists.addAll(newPlaylists)
        notifyDataSetChanged()
    }
}

