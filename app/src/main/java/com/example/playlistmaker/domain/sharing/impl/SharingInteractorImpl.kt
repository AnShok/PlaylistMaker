package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.SharingRepository

class SharingInteractorImpl(
    private val repository: SharingRepository,
) : SharingInteractor {
    override fun shareApp() {
        repository.shareApp()
    }

    override fun openSupport() {
        repository.openSupport()
    }

    override fun openTerms() {
        repository.openTerms()
    }
}

  //  private fun getShareAppLink(): String {
  //      // Нужно реализовать
  //  }
//
  //  private fun getSupportEmailData(): EmailData {
  //      // Нужно реализовать
  //  }
//
  //  private fun getTermsLink(): String {
  //      // Нужно реализовать
  //  }
//}