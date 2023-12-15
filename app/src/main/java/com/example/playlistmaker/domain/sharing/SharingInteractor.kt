package com.example.playlistmaker.domain.sharing

import android.content.Intent

interface SharingInteractor {
    fun shareApp(): Intent
    fun openSupport(): Intent
    fun openTerms(): Intent
}