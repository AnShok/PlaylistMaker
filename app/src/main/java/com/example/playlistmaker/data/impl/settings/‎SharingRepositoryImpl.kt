package com.example.playlistmaker.data.impl.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.SharingRepository

class SharingRepositoryImpl(private val context: Context) : SharingRepository {
    override fun shareApp(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT, (context.getString(R.string.url_terms_of_use))
        )
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return shareIntent
    }

    override fun openSupport(): Intent {
        val subject = context.getString(R.string.subject_mail_support)
        val message = context.getString(R.string.message_mail_support)
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("shokin3111@gmail.com"))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        supportIntent.putExtra(Intent.EXTRA_TEXT, message)
        return supportIntent
    }

    override fun openTerms(): Intent {
        val termsOfUseIntent = Intent(Intent.ACTION_VIEW)
        termsOfUseIntent.data = Uri.parse(context.getString(R.string.url_terms_of_use))
        termsOfUseIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return termsOfUseIntent
    }
}
