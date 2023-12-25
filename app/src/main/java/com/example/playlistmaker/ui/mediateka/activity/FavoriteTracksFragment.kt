package com.example.playlistmaker.ui.mediateka.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.ui.mediateka.view_model.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    // Привязка для доступа к элементам макета фрагмента
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    // ViewModel для управления данными фрагмента
    private val viewModel by viewModel<FavoriteTracksViewModel>()

    // Создание представления фрагмента
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    // Освобождение ресурсов при уничтожении фрагмента
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Статический метод для создания нового экземпляра фрагмента
    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}