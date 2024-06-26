package com.example.playlistmaker.ui.mediateka.favoriteTracks

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.search.adapters.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    // Привязка для доступа к элементам макета фрагмента
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private var favoriteAdapter = TrackAdapter()

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

        favoriteAdapter.itemClickListener = { track ->
            startAudioPlayer(track)
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
        // Сортируем треки
        val sortedTracks = tracks.sortedByDescending { it.trackId }
        with(favoriteAdapter) {
            tracksList.clear()
            tracksList.addAll(sortedTracks)
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

        val bundle = Bundle()
        bundle.putParcelable(TRACK, track)
        findNavController().navigate(
            R.id.action_mediatekaFragment_to_audioPlayerFragment,
            bundle
        )
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