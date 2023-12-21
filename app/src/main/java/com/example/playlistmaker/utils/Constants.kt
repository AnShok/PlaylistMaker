package com.example.playlistmaker.utils

// URL для доступа к iTunes API
const val ITUNES_URL = "https://itunes.apple.com"

// Ключ для сохранения истории поиска в SharedPreferences
const val SEARCH_HISTORY = "search_history"

// Ключ для сохранения текущей темы приложения в SharedPreferences
const val THEME_SHARED = "theme_shared"

// Ключ для передачи контекста между компонентами приложения
const val CONTEXT = "context"

// Ключ для сохранения статуса переключателя темы в SharedPreferences
const val APPLICATION_THEME_SWITCHER_SETTINGS = "theme_switch_status"

// Задержка для избегания многократных кликов (в миллисекундах)
const val CLICK_DEBOUNCE_DELAY = 1000L

// Задержка для избегания многократных запросов поиска (в миллисекундах)
const val SEARCH_DEBOUNCE_DELAY = 2000L

// Радиус скругления углов в dp
const val CORNER_RADIUS_DP = 8f
