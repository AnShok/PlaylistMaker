package com.example.playlistmaker.ui.player.activity

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.player.model.AudioPlayerProgressStatus
import com.example.playlistmaker.domain.player.model.AudioPlayerStatus
import com.example.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.example.playlistmaker.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioplayerBinding
    private val viewModel by viewModel<AudioPlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Кнопка для возвращения на предыдущий экран
        binding.backButton.setOnClickListener {
            finish()
        }

        // Получение трека из intent
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK, Track::class.java)!!
        } else {
            intent.getParcelableExtra(TRACK)!!
        }

        // Привязка данных трека к вьюшкам
        bindingTrackDataInActivity(track)
        viewModel.loadTrack(track)

        viewModel.audioPlayerProgressStatus.observe(this) { audioPlayerProgressStatus ->
            playbackControl(audioPlayerProgressStatus)
        }

        // Обработчик для кнопки воспроизведения/паузы
        binding.playPauseButton.setOnClickListener {
            viewModel.playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseAudioPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
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
        val cornerRadiusPx = dpToPx(cornerRadiusDp)

        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(cornerRadiusPx))
            .into(binding.coverImage)
    }

    // Перевод dp в px
    private fun dpToPx(dp: Float): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    // Управление воспроизведением
    private fun playbackControl(audioPlayerProgressStatus2: AudioPlayerProgressStatus) {
        when (audioPlayerProgressStatus2.audioPlayerStatus) {
            AudioPlayerStatus.STATE_PLAYING -> {
                binding.trackPlaybackTime.text =
                    timeFormat.format(audioPlayerProgressStatus2.currentPosition)
                binding.playPauseButton.setImageResource(R.drawable.pause_button_day)
            }

            AudioPlayerStatus.STATE_PREPARED -> binding.playPauseButton.setImageResource(R.drawable.play_button_day)
            AudioPlayerStatus.STATE_PAUSED -> binding.playPauseButton.setImageResource(R.drawable.play_button_day)
            AudioPlayerStatus.STATE_DEFAULT -> {}
            AudioPlayerStatus.STATE_ERROR -> showMassage()
        }
    }

    // Отображение сообщения
    private fun showMassage() {
        Toast.makeText(this, getString(R.string.audio_file_not_available), Toast.LENGTH_LONG).show()
    }

    private val timeFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    companion object{
        // Радиус скругления углов в dp
        private const val CORNER_RADIUS_DP = 8f
    }
}