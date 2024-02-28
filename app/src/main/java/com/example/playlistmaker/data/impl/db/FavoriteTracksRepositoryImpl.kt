package com.example.playlistmaker.data.impl.db

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.data.db.converters.TrackDbConverter
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : FavoriteTracksRepository {
    override fun getTracks(): Flow<List<Track>> = flow {
        val favoriteTracks = withContext(Dispatchers.IO) {
            appDatabase.tracksDao().getTracks()
        }
        emit(convertTrackEntitiesToTracks(favoriteTracks))
    }

    override suspend fun additionTrack(track: Track) {
        appDatabase.tracksDao().additionTrack(trackDbConverter.map(track))
    }

    override suspend fun deleteTrack(trackId: Int) {
        appDatabase.tracksDao().deleteTrackEntity(trackId)
    }

    override fun isFavoriteTrack(trackId: Int): Flow<Boolean> = flow {
        val isFavorite = withContext(Dispatchers.IO) {
            appDatabase.tracksDao().isFavoriteTrack(trackId)
        }
        emit(isFavorite)
    }

    private fun convertTrackEntitiesToTracks(track: List<TrackEntity>): List<Track> {
        return track.map { trackEntity ->
            trackDbConverter.map(trackEntity).apply {
                isFavorite = true
            }
        }
    }
}