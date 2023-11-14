package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doTrackSearchRequest(dto: TrackSearchRequest): Response
}