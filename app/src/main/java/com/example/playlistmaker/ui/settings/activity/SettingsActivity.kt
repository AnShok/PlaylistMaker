package com.example.playlistmaker.ui.settings.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModelFactory

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SettingsViewModelFactory()
        )[SettingsViewModel::class.java]
    }
    private var currentThemeChecked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Обновление состояния переключателя при изменении значения в darkThemeEnabled
        viewModel.darkThemeEnabled.observe(this) { isChecked ->
            binding.themeSwitcher.isChecked = isChecked
        }

        //обработчик событ изменения состояиния перееключателя
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            if (currentThemeChecked != checked) {
                currentThemeChecked = checked
                viewModel.setThemeToShared(checked)
                binding.root.post {
                    viewModel.switchTheme(checked)
                }
            }
        }

        //Кнопка Назад - закрытие активити
        binding.buttonBack.setOnClickListener {
            finish()
        }

        //Кнопка поделиться ссылкой
        binding.shareApp.setOnClickListener {
            viewModel.shareApp()?.let { startActivity(viewModel.settingsIntentEvent.value) }
            //viewModel.shareApp()
        }

        //Кнопка письма в поддержку с предзаполненной темой и сообщением
        binding.openSupport.setOnClickListener {
            viewModel.openSupport()?.let { startActivity(viewModel.settingsIntentEvent.value) }
            //viewModel.openSupport()
        }

        //Кнопка открытия пользовательского соглашения
        binding.openTerms.setOnClickListener {
            viewModel.openTerms()?.let { startActivity(viewModel.settingsIntentEvent.value) }
            //viewModel.openTerms()
        }
    }
}
