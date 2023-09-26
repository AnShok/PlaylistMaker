package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private val gson = Gson()

    // Загрузка истории поиска из SharedPreferences
    fun loadSearchHistory(): ArrayList<Track> {
        val historyJson = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return if (historyJson != null) {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            gson.fromJson(historyJson, type)
        } else {
            ArrayList()
        }
    }

    // Сохранение истории поиска в SharedPreferences
    fun saveSearchHistory(history: ArrayList<Track>) {
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
    fun clearSearchHistory() {
        val editor = sharedPreferences.edit()
        editor.remove(SEARCH_HISTORY_KEY)
        editor.apply()
    }

    companion object {
        private const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY"
        const val MAX_HISTORY_SIZE = 10
    }
}