package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.player.AudioPlayerInteractor

class AudioPlayerViewModelFactory(val audioPlayerInteractor: AudioPlayerInteractor) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AudioPlayerViewModel(
            audioPlayerInteractor = audioPlayerInteractor
        ) as T
    }
}