package com.example.playlistmaker.domain.sharing.impl

import android.content.Intent
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.SharingRepository

class SharingInteractorImpl(
    private val repository: SharingRepository,
) : SharingInteractor {
    override fun shareApp(): Intent {
        return repository.shareApp()
    }

    override fun openSupport(): Intent {
        return repository.openSupport()
    }

    override fun openTerms(): Intent {
        return repository.openTerms()
    }
}