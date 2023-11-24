package com.example.playlistmaker.domain.models

/**
 * Класс [TrackSearchResult] представляет результат поиска треков.
 *
 * @property tracks Список найденных треков.
 * @property resultStatus Статус результата поиска (по умолчанию [SearchStatus.DEFAULT]).
 */
data class TrackSearchResult(
    var tracks: List<Track>,
    var resultStatus: SearchStatus = SearchStatus.DEFAULT
)