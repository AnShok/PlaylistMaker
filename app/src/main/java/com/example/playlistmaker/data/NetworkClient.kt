package com.example.playlistmaker.data

import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.TrackSearchRequest

/**
 * Интерфейс NetworkClient определяет метод для выполнения запроса поиска треков.
 */
interface NetworkClient {
    suspend fun doTrackSearchRequest(dto: TrackSearchRequest): Response
}