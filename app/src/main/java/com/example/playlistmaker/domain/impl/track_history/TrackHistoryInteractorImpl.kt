package com.example.playlistmaker.domain.impl.track_history

import com.example.playlistmaker.domain.api.track_history.TrackHistoryInteractor
import com.example.playlistmaker.domain.api.track_history.TrackHistoryRepository
import com.example.playlistmaker.domain.models.Track
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