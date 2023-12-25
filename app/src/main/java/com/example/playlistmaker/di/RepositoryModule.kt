package com.example.playlistmaker.di

import com.example.playlistmaker.data.player.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.search.local.impl.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.search.network.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.settings.impl.ThemeSettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.example.playlistmaker.domain.player.AudioPlayerRepository
import com.example.playlistmaker.domain.search.TrackHistoryRepository
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.settings.ThemeSettingsRepository
import com.example.playlistmaker.domain.sharing.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    factory<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    factory<TrackHistoryRepository> {
        TrackHistoryRepositoryImpl(get(named(SEARCH_HISTORY)), get())
    }

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    factory<ThemeSettingsRepository> {
        ThemeSettingsRepositoryImpl(get(named(THEME_SHARED)))
    }

    factory<SharingRepository> {
        SharingRepositoryImpl(androidContext())
    }
}