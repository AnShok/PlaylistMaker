package com.example.playlistmaker.ui.mediateka.favoriteTracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    private val _favoriteState = MutableLiveData<FavoriteTracksStatus>(FavoriteTracksStatus.Load)
    val favoriteState: LiveData<FavoriteTracksStatus> = _favoriteState

    private fun setState(favoriteState: FavoriteTracksStatus) {
        _favoriteState.postValue(favoriteState)
    }

    fun getFavoriteList() {
        viewModelScope.launch {
            setState(FavoriteTracksStatus.Load)
            favoriteTracksInteractor
                .getTracks()
                .collect { tracks ->
                    if (tracks.isEmpty()) {
                        setState(FavoriteTracksStatus.NoEntry)
                    } else {
                        setState(FavoriteTracksStatus.Content(tracks))
                    }
                }
        }
    }

}