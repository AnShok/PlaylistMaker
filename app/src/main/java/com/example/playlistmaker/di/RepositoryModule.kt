package com.example.playlistmaker.di

import com.example.playlistmaker.data.db.converters.TrackDbConverter
import com.example.playlistmaker.data.impl.db.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.data.impl.player.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.impl.search.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.impl.search.TrackRepositoryImpl
import com.example.playlistmaker.data.impl.settings.ThemeSettingsRepositoryImpl
import com.example.playlistmaker.data.impl.settings.SharingRepositoryImpl
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
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

    factory { TrackDbConverter() }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(appDatabase = get(), trackDbConverter = get())
    }

    factory<ThemeSettingsRepository> {
        ThemeSettingsRepositoryImpl(get(named(THEME_SHARED)))
    }

    factory<SharingRepository> {
        SharingRepositoryImpl(androidContext())
    }
}