package com.example.playlistmaker.data.sharing.impl

import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.sharing.SharingRepository

class SharingRepositoryImpl (private val app: App): SharingRepository {
    override fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent . type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,(app.getString(R.string.url_terms_of_use))
        )
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        app.startActivity(shareIntent)
    }
    override fun openSupport() {
        val subject = app.getString(R.string.subject_mail_support)
        val message = app.getString(R.string.message_mail_support)
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("shokin3111@gmail.com"))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        supportIntent.putExtra(Intent.EXTRA_TEXT, message)
        app.startActivity(supportIntent)
    }
    override fun openTerms() {
        val termsOfUseIntent = Intent(Intent.ACTION_VIEW)
        termsOfUseIntent.data = Uri.parse(app.getString(R.string.url_terms_of_use))
        termsOfUseIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        app.startActivity(termsOfUseIntent)
    }
}
