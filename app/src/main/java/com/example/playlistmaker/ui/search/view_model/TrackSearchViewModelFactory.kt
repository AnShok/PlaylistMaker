package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator

class TrackSearchViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrackSearchViewModel(
            trackInteractor = Creator.provideTrackInteractor(),
            trackHistoryInteractor = Creator.provideTrackHistoryInteractor()
        ) as T
    }
}

//class TrackSearchViewModelFactory(
//    private val trackInteractor: TrackInteractor,
//    private val trackHistoryInteractor: TrackHistoryInteractor
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return TrackSearchViewModel(trackInteractor, trackHistoryInteractor) as T
//    }
//}