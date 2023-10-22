package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var themeSwitcher: SwitchMaterial
    private lateinit var settingsThemeManager: SettingsThemeManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsThemeManager = SettingsThemeManager(this)

        //Нахождение элементов интерфейса
        val backButton = findViewById<Button>(R.id.button_back)
        val shareButton = findViewById<ImageView>(R.id.ic_share_app)
        val supportButton = findViewById<ImageView>(R.id.ic_write_to_support)
        val termsOfUseButton = findViewById<ImageView>(R.id.ic_terms_of_use)
        themeSwitcher = findViewById(R.id.theme_switcher)

        //определение текущей темы
        val isDarkThemeEnabled = settingsThemeManager.isDarkThemeEnabled()

        //Установка состояния переключателя
        themeSwitcher.isChecked = isDarkThemeEnabled

        //обработчик событ изменения состояиния перееключателя
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            //вызоы меотда switchTheme
            (applicationContext as App).switchTheme(checked)
        }

        //Кнопка Назад - закрытие активити
        backButton.setOnClickListener {
            finish()
        }

        //Кнопка поделиться ссылкой
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent . type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,(getString(R.string.url_terms_of_use))
            )
            startActivity(shareIntent)
        }

        //Кнопка письма в поддержку с предзаполненной темой и сообщением
        supportButton.setOnClickListener {
            val subject = getString(R.string.subject_mail_support)
            val message = getString(R.string.message_mail_support)
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("shokin3111@gmail.com"))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            supportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(supportIntent)
        }

        //Кнопка открытия пользовательского соглашения
        termsOfUseButton.setOnClickListener {
            val termsOfUseIntent = Intent(Intent.ACTION_VIEW)
            termsOfUseIntent.data = Uri.parse(getString(R.string.url_terms_of_use))
            startActivity(termsOfUseIntent)
        }
    }
}
