package com.example.playlistmaker.ui.settings.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModelFactory
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SettingsViewModelFactory())[SettingsViewModel::class.java]

        // Определение текущей темы
        val isDarkThemeEnabled: Boolean = viewModel.darkThemeEnabled.value ?: false

        //Установка состояния переключателя
        binding.themeSwitcher.isChecked = isDarkThemeEnabled

        //обработчик событ изменения состояиния перееключателя
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.setThemeToShared(checked)
            viewModel.switchTheme(checked)
        }

        //Кнопка Назад - закрытие активити
        binding.buttonBack.setOnClickListener {
            finish()
        }

        //Кнопка поделиться ссылкой
        binding.shareApp.setOnClickListener {
            viewModel.shareApp()
        }

        //Кнопка письма в поддержку с предзаполненной темой и сообщением
        binding.openSupport.setOnClickListener {
            viewModel.openSupport()
        }

        //Кнопка открытия пользовательского соглашения
        binding.openTerms.setOnClickListener {
            viewModel.openTerms()
        }
    }
}
