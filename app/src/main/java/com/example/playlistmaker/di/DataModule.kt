package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.app.App
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.AppDatabasePlaylists
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

    single {
        Room.databaseBuilder(androidContext(), AppDatabasePlaylists::class.java, PLALISTS_DATABASE)
            .fallbackToDestructiveMigration()
            .build()
    }

}

// URL для доступа к iTunes API
private const val ITUNES_URL = "https://itunes.apple.com"

// Ключ для сохранения истории поиска в SharedPreferences
private const val SEARCH_HISTORY = "search_history"

// Ключ для сохранения текущей темы приложения в SharedPreferences
private const val THEME_SHARED = "theme_shared"

// Ключ для передачи контекста между компонентами приложения
private const val CONTEXT = "context"

// Константа, содержащая имя базы данных, используемой для сохранения информации об избранных треках в приложении.
private const val FAVORITE_DATABASE = "database.db"

private const val PLALISTS_DATABASE = "playlistsDatabase.db"