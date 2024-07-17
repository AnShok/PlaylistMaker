package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = PLAYLISTS_TABLET)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val coverPath: String,
    val tracksIds: String,
    val tracks: ArrayList<Long>,
    val tracksAmount: Int,
    val imageUri: String?
)

const val PLAYLISTS_TABLET = "playlists_tablet"