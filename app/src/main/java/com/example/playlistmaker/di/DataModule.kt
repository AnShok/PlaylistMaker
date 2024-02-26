package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.app.App
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.search.network.ItunesApi
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ItunesApi> {
        Retrofit.Builder()
            .baseUrl(ITUNES_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }

    single(named(SEARCH_HISTORY)) {
        androidContext()
            .getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE)
    }

    single(named(THEME_SHARED)) {
        androidContext()
            .getSharedPreferences(THEME_SHARED, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    factory {
        MediaPlayer()
    }

    factory<NetworkClient> {
        RetrofitNetworkClient(get(),)
    }

    single(named(CONTEXT)) {
        App()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, FAVORITE_DATABASE)
            .fallbackToDestructiveMigration()
            .build()
    }

}