package com.example.playlistmaker.data.dto

/**
 * Data-класс, представляющий модель данных запроса на поиск треков.
 * поле expression, представляет строку поискового запроса.
 */
data class TrackSearchRequest(
    val expression: String
)