package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doTrackSearchRequest(dto: Any): Response
}