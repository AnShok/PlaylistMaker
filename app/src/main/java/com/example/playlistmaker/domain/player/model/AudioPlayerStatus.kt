package com.example.playlistmaker.domain.player.model

/**
 * Enum-класс, представляющий возможные статусы аудиоплеера.
 */
enum class AudioPlayerStatus {
    STATE_DEFAULT,
    STATE_PREPARED,
    STATE_PLAYING,
    STATE_PAUSED,
    STATE_ERROR
}