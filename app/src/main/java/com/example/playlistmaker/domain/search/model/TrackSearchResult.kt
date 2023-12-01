package com.example.playlistmaker.domain.search.model

/**
 * Класс [TrackSearchResult] представляет результат поиска треков.
 *
 * @property tracks Список найденных треков.
 * @property resultStatus Статус результата поиска (по умолчанию [SearchStatus.DEFAULT]).
 */
data class TrackSearchResult(
    val tracks: List<Track>,
    var resultStatus: SearchStatus = SearchStatus.DEFAULT
)