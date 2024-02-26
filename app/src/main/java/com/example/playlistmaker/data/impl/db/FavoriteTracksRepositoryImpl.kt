package com.example.playlistmaker.data.impl.db

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.data.db.converters.TrackDbConverter
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase, private val trackDbConverter: TrackDbConverter
) : FavoriteTracksRepository {
    override suspend fun getTracks(): Flow<List<Track>> = flow {
        val favoriteTracks = appDatabase.tracksDao().getTracks()
        emit(converterForEntity(favoriteTracks))
    }

    override suspend fun additionTrack(track: Track) {
        appDatabase.tracksDao().additionTrack(trackDbConverter.map(track))
    }

    override suspend fun deleteTrack(trackId: Int) {
        appDatabase.tracksDao().deleteTrackEntity(trackId)
    }

    override suspend fun isFavoriteTrack(trackId: Int): Flow<Boolean> = flow {
        val isFavorite = appDatabase.tracksDao().isFavoriteTrack(trackId)
        emit(isFavorite)
    }

    private fun converterForEntity(track: List<TrackEntity>): List<Track> {
        return track.map { track -> trackDbConverter.map(track) }
    }
}