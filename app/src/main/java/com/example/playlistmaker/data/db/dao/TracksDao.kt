package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.TrackEntity

@Dao
interface TracksDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun additionTrack(tracks: TrackEntity)

    @Query("DELETE FROM favorite_tracks_tablet WHERE trackId = :trackId")
    suspend fun deleteTrackEntity(trackId: Int)

    @Query("SELECT * FROM favorite_tracks_tablet ORDER BY additionTime DESC")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_tracks_tablet  WHERE trackId = :trackId)")
    suspend fun isFavoriteTrack(trackId: Int): Boolean

}