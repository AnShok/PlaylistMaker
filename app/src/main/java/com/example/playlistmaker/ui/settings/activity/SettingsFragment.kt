package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Обновление состояния переключателя при изменении значения в darkThemeEnabled
        viewModel.isThemeSwitcherEnabled.observe(viewLifecycleOwner) { isChecked ->
            binding.themeSwitcher.isChecked = isChecked
        }
        viewModel.settingsIntentEvent.observe(viewLifecycleOwner) { intent ->
            startActivity(intent)
        }

        //обработчик событ изменения состояиния перееключателя
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.onThemeSwitcherChecked(checked)
        }

        //Кнопка поделиться ссылкой
        binding.shareApp.setOnClickListener {
            viewModel.onShareClick()
        }

        //Кнопка письма в поддержку с предзаполненной темой и сообщением
        binding.openSupport.setOnClickListener {
            viewModel.onSupportClick()
        }

        //Кнопка открытия пользовательского соглашения
        binding.openTerms.setOnClickListener {
            viewModel.onTermsClick()
        }
    }
}
