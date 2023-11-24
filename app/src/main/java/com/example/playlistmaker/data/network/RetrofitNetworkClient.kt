package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.domain.models.SearchStatus
import com.example.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Реализация интерфейса NetworkClient с использованием Retrofit.
 * Отправляет запрос на поиск треков к сервису iTunes API.
 */
class RetrofitNetworkClient : NetworkClient {

    // Базовый URL для iTunes API
    private val itunesBaseUrl = "https://itunes.apple.com"

    // Создание объекта Retrofit с указанием базового URL и конвертера JSON
    private val retrofit =
        Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()

    // Создание экземпляра сервиса для взаимодействия с API
    private val itunesService = retrofit.create(ItunesApi::class.java)

    /**
     * Отправляет запрос на поиск треков к iTunes API.
     *
     * @param dto Объект, представляющий запрос на поиск треков.
     * @return Response с результатами поиска.
     */
    override fun doTrackSearchRequest(dto: TrackSearchRequest): Response {
        if (dto is TrackSearchRequest) {
            try {
                val resp = itunesService.search(dto.expression).execute()
                val body = resp.body() ?: Response()

                return body.apply { resultStatus = SearchStatus.RESPONSE_RECEIVED }
            } catch (e: Exception) {
                // Обработка ошибки, вывод в лог
                e.printStackTrace()

                // Возвращаем результат с ошибкой
                return Response().apply { resultStatus = SearchStatus.NETWORK_ERROR }
            }
        }
        // Возвращаем результат с ошибкой, если dto не является объектом TrackSearchRequest
        return Response().apply { resultStatus = SearchStatus.NETWORK_ERROR }
    }
}

