package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest

/**
 * Интерфейс NetworkClient определяет метод для выполнения запроса поиска треков.
 */
interface NetworkClient {
    fun doTrackSearchRequest(dto: TrackSearchRequest): Response
}