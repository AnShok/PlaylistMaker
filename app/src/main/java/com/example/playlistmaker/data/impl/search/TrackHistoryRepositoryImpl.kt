package com.example.playlistmaker.data.impl.search

import android.content.SharedPreferences
import com.example.playlistmaker.domain.search.TrackHistoryRepository
import com.example.playlistmaker.domain.search.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackHistoryRepositoryImpl(
    val sharedPreferences: SharedPreferences,
    val gson: Gson
) : TrackHistoryRepository {

    // добавление трека в историю поиска
    override fun addToSearchHistory(track: Track) {
        val history = loadSearchHistory()
        history.removeAll { it == track }
        history.add(0, track)
        if (history.size > MAX_HISTORY_SIZE) {
            history.removeAt(MAX_HISTORY_SIZE)
        }
        saveSearchHistory(history)
    }

    // Загрузка истории поиска из SharedPreferences
    override fun loadSearchHistory(): ArrayList<Track> {
        val historyJson = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return if (historyJson != null) {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            gson.fromJson(historyJson, type)
        } else {
            ArrayList()
        }
    }

    // Сохранение истории поиска в SharedPreferences
    override fun saveSearchHistory(history: ArrayList<Track>) {
        val historyToSave = if (history.size > MAX_HISTORY_SIZE) {
            ArrayList(history.subList(0, MAX_HISTORY_SIZE))
        } else {
            history
        }
        val historyJson = gson.toJson(historyToSave)
        val editor = sharedPreferences.edit()
        editor.putString(SEARCH_HISTORY_KEY, historyJson)
        editor.apply()
    }

    // Очистка истории
    override fun clearSearchHistory() {
        val editor = sharedPreferences.edit()
        editor.remove(SEARCH_HISTORY_KEY)
        editor.apply()
    }

    companion object {
        // Максимальный размер истории поиска
        private const val MAX_HISTORY_SIZE = 10

        // Ключ для сохранения истории поиска в SharedPreferences
        private const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY"
    }
}