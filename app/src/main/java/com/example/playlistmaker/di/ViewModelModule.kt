package com.example.playlistmaker.di

import com.example.playlistmaker.ui.mediateka.favoriteTracks.FavoriteTracksViewModel
import com.example.playlistmaker.ui.mediateka.playlists.CurrentPlaylistViewModel
import com.example.playlistmaker.ui.mediateka.playlists.ModifyPlaylistViewModel
import com.example.playlistmaker.ui.mediateka.playlists.NewPlaylistViewModel
import com.example.playlistmaker.ui.mediateka.playlists.PlayListViewModel
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.search.view_model.TrackSearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        TrackSearchViewModel(get(), get())
    }

    viewModel {
        AudioPlayerViewModel(get(), get(), get(), get())
    }

    viewModel {
        PlayListViewModel(get())
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

    viewModel {
        CurrentPlaylistViewModel(get())
    }
    viewModel {
        ModifyPlaylistViewModel(get())
    }
    single {
        FirebaseAnalytics.getInstance(androidContext())
    }
}