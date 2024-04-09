package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.data.db.converters.TracksIdsConverter
import com.example.playlistmaker.data.db.dao.PlaylistsDao

@Database(version = 1, entities = [PlaylistEntity::class, TrackToPlaylistEntity::class])
@TypeConverters(TracksIdsConverter::class)
abstract class AppDatabasePlaylists : RoomDatabase() {
    abstract fun playlistsDao(): PlaylistsDao
}