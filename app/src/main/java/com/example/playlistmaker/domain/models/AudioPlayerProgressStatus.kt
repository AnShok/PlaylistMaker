package com.example.playlistmaker.domain.models

/**
 * Класс, представляющий статус и текущую позицию аудиоплеера.
 * @property audioPlayerStatus - текущий статус аудиоплеера.
 * @property currentPosition - текущая позиция воспроизведения (в миллисекундах).
 */
data class AudioPlayerProgressStatus(
    val audioPlayerStatus: AudioPlayerStatus,
    val currentPosition: Int = 0
)