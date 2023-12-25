package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.domain.search.model.SearchStatus
import com.example.playlistmaker.data.search.dto.TrackSearchRequest

/**
 * Реализация интерфейса NetworkClient с использованием Retrofit.
 * Отправляет запрос на поиск треков к сервису iTunes API.
 */
class RetrofitNetworkClient(private val itunesService: ItunesApi) : NetworkClient {

    /**
     * Отправляет запрос на поиск треков к iTunes API.
     *
     * @param dto Объект, представляющий запрос на поиск треков.
     * @return Response с результатами поиска.
     */
    override fun doTrackSearchRequest(dto: TrackSearchRequest): Response {
        if (true) { //dto is TrackSearchRequest
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

