package com.example.playlistmaker.di

import com.example.playlistmaker.ui.mediateka.favoriteTracks.FavoriteTracksViewModel
import com.example.playlistmaker.ui.mediateka.playlists.NewPlaylistViewModel
import com.example.playlistmaker.ui.mediateka.playlists.PlaylistViewModel
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.search.view_model.TrackSearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        TrackSearchViewModel(get(), get())
    }

    viewModel {
        AudioPlayerViewModel(get(), get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel {
        NewPlaylistViewModel(get())
    }

    viewModel {
        FavoriteTracksViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
}