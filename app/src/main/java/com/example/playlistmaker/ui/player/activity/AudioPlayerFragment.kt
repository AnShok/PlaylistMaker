package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioplayerBinding
import com.example.playlistmaker.domain.db.models.FavoriteTracksStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.player.model.AudioPlayerProgressStatus
import com.example.playlistmaker.domain.player.model.AudioPlayerStatus
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.example.playlistmaker.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerFragment : Fragment() {
    private var _binding: FragmentAudioplayerBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AudioPlayerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        // Кнопка для возвращения на предыдущий экран
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Получение трека из intent
        val track = requireArguments().getParcelable<Track>(TRACK)
            ?: throw IllegalArgumentException("Track must not be null")

        // Привязка данных трека к вьюшкам
        bindingTrackDataInActivity(track)
        viewModel.loadTrack(track)

        viewModel.audioPlayerProgressStatus.observe(viewLifecycleOwner) { audioPlayerProgressStatus ->
            playbackControl(audioPlayerProgressStatus)
        }

        // Обработчик для кнопки воспроизведения/паузы
        binding.playPauseButton.setOnClickListener {
            viewModel.playbackControl()
        }
        viewModel.isFavorite(track)
        viewModel.favoriteState.observe(viewLifecycleOwner) { isFavorite ->
            like(isFavorite)
        }

        binding.addToFavoriteButton.setOnClickListener {
            viewModel.clickOnFavorite(track)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseAudioPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.destroyMediaPlayer()
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
    private fun playbackControl(audioPlayerProgressStatus: AudioPlayerProgressStatus) {
        when (audioPlayerProgressStatus.audioPlayerStatus) {
            AudioPlayerStatus.STATE_PLAYING -> {
                binding.trackPlaybackTime.text =
                    timeFormat.format(audioPlayerProgressStatus.currentPosition)
                binding.playPauseButton.setImageResource(R.drawable.pause_button_day)
            }

            AudioPlayerStatus.STATE_PREPARED -> binding.playPauseButton.setImageResource(R.drawable.play_button_day)
            AudioPlayerStatus.STATE_PAUSED -> binding.playPauseButton.setImageResource(R.drawable.play_button_day)
            AudioPlayerStatus.STATE_DEFAULT ->  {
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

    // Отображение сообщения
    private fun showMassage() {
        Toast.makeText(requireContext(), getString(R.string.audio_file_not_available), Toast.LENGTH_LONG).show()
    }

    private val timeFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    companion object {
        // Радиус скругления углов в dp
        private const val CORNER_RADIUS_DP = 8f
    }
}