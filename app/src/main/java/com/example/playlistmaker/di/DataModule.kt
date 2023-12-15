package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.app.App
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.local.impl.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.search.network.ItunesApi
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.search.TrackHistoryRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ItunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }

    single(named("search_history")) {
        androidContext()
            .getSharedPreferences("search_history", Context.MODE_PRIVATE)
    }

    single(named("themeShared")) {
        androidContext()
            .getSharedPreferences("theme_shared_preferences", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    factory {
        MediaPlayer()
    }

    factory<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single(named("context")) {
        App()
    }

}