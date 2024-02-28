package com.example.playlistmaker.ui.mediateka.favoriteTracks

import com.example.playlistmaker.domain.search.model.Track

sealed interface FavoriteTracksStatus {

    data class Content(val tracks: List<Track>) : FavoriteTracksStatus

    data object NoEntry : FavoriteTracksStatus

    data object Load : FavoriteTracksStatus
}