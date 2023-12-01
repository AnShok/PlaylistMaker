package com.example.playlistmaker.data.search.dto

import com.example.playlistmaker.domain.search.model.SearchStatus

/**
 * Базовый класс для представления результатов запросов в сеть.
 * Содержит поле [resultStatus], указывающее на статус запроса, который может принимать
 * значения из перечисления [SearchStatus] (например, DEFAULT, RESPONSE_RECEIVED, NETWORK_ERROR).
 */
open class Response() {
    var resultStatus: SearchStatus = SearchStatus.DEFAULT
}
