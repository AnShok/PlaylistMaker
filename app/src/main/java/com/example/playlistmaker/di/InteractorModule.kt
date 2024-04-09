package com.example.playlistmaker.di

import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.PlaylistsInteractor
import com.example.playlistmaker.domain.db.impl.FavoriteTracksInteractorImpl
import com.example.playlistmaker.domain.db.impl.PlaylistsInteractorImpl
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.search.TrackHistoryInteractor
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.settings.ThemeSettingsInteractor
import com.example.playlistmaker.domain.settings.impl.ThemeSettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    factory<TrackHistoryInteractor> {
        TrackHistoryInteractorImpl(get())
    }

    factory<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }

    factory<ThemeSettingsInteractor> {
        ThemeSettingsInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    factory<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(favoriteTracksRepository = get())
    }

    single<PlaylistsInteractor> {
        PlaylistsInteractorImpl(playlistsRepository = get())
    }
}