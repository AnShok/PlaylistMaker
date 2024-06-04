package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.PlaylistEntity
import com.example.playlistmaker.domain.search.model.Playlist

class PlaylistsDbConverter {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverPath,
            playlist.tracksIds,
            playlist.tracksAmount,
            playlist.imageUri
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.coverPath,
            playlist.tracksIds,
            playlist.tracksAmount,
            playlist.imageUri
        )
    }
}