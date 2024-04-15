package com.example.playlistmaker.ui.player.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioplayerBinding
import com.example.playlistmaker.domain.db.models.FavoriteTracksStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.player.model.AudioPlayerStatus
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.PlaylistState
import com.example.playlistmaker.ui.main.MainActivity
import com.example.playlistmaker.ui.player.adapters.AudioPlayerAdapter
import com.example.playlistmaker.ui.player.adapters.AudioPlayerViewHolder
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.example.playlistmaker.ui.search.activity.SearchFragment
import com.example.playlistmaker.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerFragment : Fragment(), AudioPlayerViewHolder.ClickListener {
    private var _binding: FragmentAudioplayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var track: Track
    private lateinit var trackPreviewUrl: String
    private val viewModel by viewModel<AudioPlayerViewModel>()
    private lateinit var adapter: AudioPlayerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = arguments?.getParcelable<Track>(SearchFragment.TRACK) as Track

        (activity as MainActivity).hideNavBar()

        bindingTrackDataInActivity(track)

        adapter = AudioPlayerAdapter(this)

        binding.bottomSheetRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.bottomSheetRecyclerView.adapter = adapter

        val bottomSheetState = BottomSheetBehavior.from(binding.bottomSheetAudioPlayer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetState.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.bottomSheetOverlay.visibility = View.GONE
                    }

                    else -> {
                        adapter.notifyDataSetChanged()
                        binding.bottomSheetOverlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.addToPlaylistButton.setOnClickListener {
            bottomSheetState.state = BottomSheetBehavior.STATE_COLLAPSED
            adapter.notifyDataSetChanged()
        }

        binding.bottomSheetAddPlaylistBtn.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
        }

        viewModel.isFavorite(track)

        viewModel.getPlaylists()

        viewModel.audioPlayerProgressStatus.observe(viewLifecycleOwner) { state ->
            binding.trackPlaybackTime.text =
                android.icu.text.SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(state.currentPosition)
            playbackControl(state.audioPlayerStatus)
        }

        viewModel.playlistState.onEach { state -> playlistStateManage(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.favoriteState.observe(viewLifecycleOwner) { isFavorite ->
            like(isFavorite)
        }

        binding.addToFavoriteButton.setOnClickListener {
            viewModel.clickOnFavorite(track)
        }

        binding.playPauseButton.setOnClickListener {
            viewModel.playControl()
        }

        viewModel.setPlayer(track)

        binding.toolbarplayer.setNavigationOnClickListener {
            findNavController().navigateUp()
            (activity as? MainActivity)?.showNavBar()
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as? MainActivity)?.showNavBar()
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDestroy()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
        binding.playPauseButton.setImageResource(R.drawable.play_button_day)
    }

    // Привязка данных трека к вьюшкам
    private fun bindingTrackDataInActivity(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTimeMillis.text = timeFormat.format(track.trackTimeMillis)
        binding.albumName.text = track.collectionName.orEmpty()
        binding.releaseYearName.text = Utils.formattedReleaseDate(track.releaseDate)
        binding.genreName.text = track.primaryGenreName
        binding.countryName.text = track.country

        // Улучшение качества обложки
        val artworkUrl512 = Utils.getArtworkUrl512(track.artworkUrl100)
        // Скругление обложки
        val cornerRadiusDp = CORNER_RADIUS_DP
        val cornerRadiusPx = Utils.dpToPx(cornerRadiusDp, resources)

        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(cornerRadiusPx))
            .into(binding.coverImage)
    }

    // Управление воспроизведением
    private fun playbackControl(audioPlayerStatus: AudioPlayerStatus) {
        when (audioPlayerStatus) {
            AudioPlayerStatus.STATE_PLAYING -> {
                binding.trackPlaybackTime.text =
                    android.icu.text.SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(viewModel.audioPlayerProgressStatus.value?.currentPosition)
                binding.playPauseButton.setImageResource(R.drawable.pause_button_day)
            }

            AudioPlayerStatus.STATE_PAUSED -> binding.playPauseButton.setImageResource(R.drawable.play_button_day)
            AudioPlayerStatus.STATE_PREPARED -> binding.playPauseButton.setImageResource(R.drawable.play_button_day)
            AudioPlayerStatus.STATE_DEFAULT -> {
                binding.playPauseButton.setImageResource(R.drawable.play_button_day)
                binding.trackPlaybackTime.setText(R.string.timer)
            }

            AudioPlayerStatus.STATE_ERROR -> showMassage()
        }
    }

    private fun like(isFavoriteState: FavoriteTracksStatus) {
        when (isFavoriteState) {
            FavoriteTracksStatus(true) -> binding.addToFavoriteButton.setImageResource(R.drawable.ic_favorite_button_pressed)
            FavoriteTracksStatus(false) -> binding.addToFavoriteButton.setImageResource(R.drawable.ic_favorite_button)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun playlistStateManage(state: PlaylistState) {
        when (state) {
            is PlaylistState.Empty -> {
                binding.bottomSheetRecyclerView.visibility = View.GONE
            }

            is PlaylistState.Data -> {
                val playlists = state.tracks
                binding.bottomSheetRecyclerView.visibility = View.VISIBLE
                adapter.playlists = playlists as ArrayList<Playlist>
                adapter.notifyDataSetChanged()
            }

            else -> {}
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(playlist: Playlist) {
        if (!viewModel.inPlaylist(
                playlist = playlist,
                trackId = track.trackId?.toLong() ?: 0
            )
        ) {
            viewModel.clickOnAddtoPlaylist(playlist = playlist, track = track)
            Toast.makeText(
                requireContext().applicationContext,
                "${getString(R.string.added_to_playlist)} ${playlist.name}",
                Toast.LENGTH_SHORT
            )
                .show()
            playlist.tracksAmount = playlist.tracksIds.size
            BottomSheetBehavior.from(binding.bottomSheetAudioPlayer).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        } else {
            Toast.makeText(
                requireContext().applicationContext,
                "${getString(R.string.already_in_playlist)} ${playlist.name}",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        BottomSheetBehavior.from(binding.bottomSheetAudioPlayer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    // Отображение сообщения
    private fun showMassage() {
        Toast.makeText(
            requireContext(),
            getString(R.string.audio_file_not_available),
            Toast.LENGTH_LONG
        ).show()
    }

    private val timeFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    companion object {
        // Радиус скругления углов в dp
        private const val CORNER_RADIUS_DP = 8f
    }
}