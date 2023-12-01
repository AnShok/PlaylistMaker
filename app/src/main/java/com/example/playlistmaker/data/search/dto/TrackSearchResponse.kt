package com.example.playlistmaker.data.search.dto

/**
 * Data-класс, представляющий модель данных ответа на поиск треков.
 * results- представляющее список объектов TrackDto, содержащих информацию о найденных треках.
 * Унаследован от класса Response.
 */
class TrackSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()