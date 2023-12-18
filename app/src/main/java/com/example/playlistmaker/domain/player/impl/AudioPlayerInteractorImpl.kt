package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.AudioPlayerRepository
import com.example.playlistmaker.domain.player.model.AudioPlayerProgressStatus
import com.example.playlistmaker.domain.search.model.Track

class AudioPlayerInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerInteractor {
    override fun preparePlayer(track: Track) {
        audioPlayerRepository.preparePlayer(track)
    }

    override fun startPlayer() {
        audioPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        audioPlayerRepository.pausePlayer()
    }

    override fun getAudioPlayerProgressStatus(): AudioPlayerProgressStatus {
        return audioPlayerRepository.getAudioPlayerProgressStatus()
    }

    override fun destroyPlayer() {
        audioPlayerRepository.destroyPlayer()
    }

}