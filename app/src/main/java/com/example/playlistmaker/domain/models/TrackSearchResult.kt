package com.example.playlistmaker.domain.models

import com.example.playlistmaker.data.dto.Response

data class TrackSearchResult(
    var tracks: List<Track>
) : Response()