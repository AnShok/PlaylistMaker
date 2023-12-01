package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.TrackHistoryRepository
import com.example.playlistmaker.domain.search.model.Track
/**
 * Реализация интерфейса [TrackHistoryInteractor] для взаимодействия с историей поиска треков.
 *
 * @param trackHistoryRepository Репозиторий истории поиска треков.
 */
class TrackHistoryInteractorImpl(private val trackHistoryRepository: TrackHistoryRepository) :
    TrackHistoryInteractor {


    override fun addToSearchHistory(track: Track) {
        trackHistoryRepository.addToSearchHistory(track)
    }

    override fun loadSearchHistory(): ArrayList<Track> {
        return trackHistoryRepository.loadSearchHistory()
    }
    override fun saveSearchHistory(history: ArrayList<Track>){
        trackHistoryRepository.saveSearchHistory(history)
    }

    override fun clearSearchHistory() {
        trackHistoryRepository.clearSearchHistory()
    }
}