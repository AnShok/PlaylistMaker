package com.example.playlistmaker.ui.mediateka.favoriteTracks

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.player.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.adapters.SearchTracksAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    // Привязка для доступа к элементам макета фрагмента
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private var favoriteAdapter = SearchTracksAdapter()

    // ViewModel для управления данными фрагмента
    private val viewModel by viewModel<FavoriteTracksViewModel>()

    // Создание представления фрагмента
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteList()
        binding.favoriteListEmpty.visibility = View.GONE
        binding.favoriteRecyclerView.adapter = favoriteAdapter

        favoriteAdapter.itemClickListener = object : SearchTracksAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                startAudioPlayer(track)
            }
        }

        viewModel.favoriteState.observe(viewLifecycleOwner) {
            execute(it)
        }
    }

    private fun execute(favoriteTracksStatus: FavoriteTracksStatus) {
        when (favoriteTracksStatus) {
            is FavoriteTracksStatus.Content -> showFavoritesList(favoriteTracksStatus.tracks)
            is FavoriteTracksStatus.NoEntry -> showEmptyFavorite()
            is FavoriteTracksStatus.Load -> showLoading()
            else -> {}
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showFavoritesList(tracks: List<Track>) {
        binding.favoriteListEmpty.visibility = View.GONE
        binding.favoriteProgressBar.visibility = View.GONE
        binding.favoriteRecyclerView.visibility = View.VISIBLE
        with(favoriteAdapter) {
            searchTracks.clear()
            searchTracks.addAll(tracks)
            notifyDataSetChanged()
        }
    }

    private fun showEmptyFavorite() {
        binding.favoriteListEmpty.visibility = View.VISIBLE
        binding.favoriteProgressBar.visibility = View.GONE
        binding.favoriteRecyclerView.visibility = View.GONE
    }

    private fun showLoading() {
        binding.favoriteListEmpty.visibility = View.GONE
        binding.favoriteProgressBar.visibility = View.VISIBLE
        binding.favoriteRecyclerView.visibility = View.GONE
    }

    private fun startAudioPlayer(track: Track) {

        //Интент для перехода на экран аудиоплеера
        val audioPlayerIntent = Intent(requireContext(), AudioPlayerActivity::class.java)

        //Данные о треке
        audioPlayerIntent.putExtra(TRACK, track)

        // Добавление флага FLAG_ACTIVITY_SINGLE_TOP
        audioPlayerIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(audioPlayerIntent)
    }

    // Освобождение ресурсов при уничтожении фрагмента
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteList()
    }

    // Статический метод для создания нового экземпляра фрагмента
    companion object {
        fun newInstance() = FavoriteTracksFragment()
        const val TRACK = "track"
    }
}