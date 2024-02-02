package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.domain.search.model.SearchStatus
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Реализация интерфейса NetworkClient с использованием Retrofit.
 * Отправляет запрос на поиск треков к сервису iTunes API.
 */
class RetrofitNetworkClient(private val itunesService: ItunesApi) : NetworkClient {

    override suspend fun doTrackSearchRequest(dto: TrackSearchRequest): Response {
        return if (true) { //dto is TrackSearchRequest
            withContext(Dispatchers.IO) {
                try {
                    val resp = itunesService.search(dto.expression)
                    resp.apply {
                        resultStatus = SearchStatus.RESPONSE_RECEIVED
                    }
                } catch (e: Exception) {
                    // Обработка ошибки, вывод в лог
                    e.printStackTrace()

                    // Возвращаем результат с ошибкой
                    Response().apply { resultStatus = SearchStatus.NETWORK_ERROR }
                }
            }
            // Возвращаем результат с ошибкой, если dto не является объектом TrackSearchRequest
        } else {
            Response().apply { resultStatus = SearchStatus.NETWORK_ERROR }
        }
    }
}