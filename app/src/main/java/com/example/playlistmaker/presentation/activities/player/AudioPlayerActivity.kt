package com.example.playlistmaker.presentation.activities.player

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.AudioPlayerProgressStatus
import com.example.playlistmaker.domain.models.AudioPlayerStatus
import com.example.playlistmaker.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Активити для воспроизведения аудиофайлов. Использует аудиоплеер и отображает информацию о текущем треке.
 */
class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioplayerBinding
    private lateinit var audioPlayerProgressStatus: AudioPlayerProgressStatus

    // Интерактор для работы с аудиоплеером
    private var audioPlayerInteractor = Creator.provideAudioPlayerInteractor()

    // Обновление прогресса воспроизведения
    private val updateProgressHandler = Handler(Looper.getMainLooper())

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
            intent.getParcelableExtra("track", Track::class.java)!!
        } else {
            intent.getParcelableExtra("track")!!
        }

        // Привязка данных трека к вьюшкам
        bindingTrackDataInActivity(track)

        // Обработчик для кнопки воспроизведения/паузы
        binding.playPauseButton.setOnClickListener {
            audioPlayerProgressStatus = audioPlayerInteractor.getAudioPlayerProgressStatus()
            playbackControl()
        }

        // Подготовка аудиоплеера и получение текущего статуса воспроизведения
        audioPlayerInteractor.preparePlayer(track)
        audioPlayerProgressStatus = audioPlayerInteractor.getAudioPlayerProgressStatus()
        if (audioPlayerProgressStatus.audioPlayerStatus == AudioPlayerStatus.STATE_ERROR) {
            showMassage()
        }
    }

    override fun onPause() {
        audioPlayerInteractor.pausePlayer()
        super.onPause()
    }

    override fun onDestroy() {
        audioPlayerInteractor.destroyPlayer()
        updateProgressHandler.removeCallbacks(updateProgressRunnable())
        super.onDestroy()
    }

    // Обновление прогресса воспроизведения
    private fun updateProgressRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                audioPlayerProgressStatus = audioPlayerInteractor.getAudioPlayerProgressStatus()

                when (audioPlayerProgressStatus.audioPlayerStatus) {
                    AudioPlayerStatus.STATE_PLAYING -> {
                        updateProgressHandler.postDelayed(this, 300)
                        binding.trackPlaybackTime.text =
                            SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                                audioPlayerProgressStatus.currentPosition
                            )
                    }

                    AudioPlayerStatus.STATE_PAUSED -> {
                        updateProgressHandler.removeCallbacks(this)
                    }

                    else -> {
                        updateProgressHandler.removeCallbacks(this)
                        binding.trackPlaybackTime.text = "00:00"
                    }
                }

                // Обновление иконки кнопки воспроизведения/паузы
                binding.playPauseButton.setImageResource(
                    if (audioPlayerProgressStatus.audioPlayerStatus == AudioPlayerStatus.STATE_PLAYING) {
                        R.drawable.pause_button_day
                    } else {
                        R.drawable.play_button_day
                    }
                )
            }
        }
    }


    // Привязка данных трека к вьюшкам
    private fun bindingTrackDataInActivity(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTimeMillis.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.albumName.text = track.collectionName ?: ""
        binding.releaseYearName.text = Utils.formattedReleaseDate(track.releaseDate)
        binding.genreName.text = track.primaryGenreName
        binding.countryName.text = track.country

        // Улучшение качества обложки
        val artworkUrl512 = Utils.getArtworkUrl512(track.artworkUrl100)
        // Скругление обложки
        val cornerRadiusDp = 8f
        val cornerRadiusPx = dpToPx(cornerRadiusDp)

        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(cornerRadiusPx))
            .into(binding.coverImage)

        // Обработчик для кнопки воспроизведения/паузы
        binding.playPauseButton.setOnClickListener {
            playbackControl()
        }
    }

    // Перевод dp в px
    private fun dpToPx(dp: Float): Int {
        val density = resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }

    // Запуск воспроизведения аудиоплеера
    private fun startPlayer() {
        audioPlayerInteractor.startPlayer()
        audioPlayerProgressStatus = audioPlayerInteractor.getAudioPlayerProgressStatus()
        updateProgressHandler.post(updateProgressRunnable())
        binding.playPauseButton.setImageResource(R.drawable.pause_button_day)
    }

    // Пауза воспроизведения аудиоплеера
    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        updateProgressHandler.removeCallbacks(updateProgressRunnable())
        binding.playPauseButton.setImageResource(R.drawable.play_button_day)
    }

    // Управление воспроизведением
    private fun playbackControl() {
        when (audioPlayerProgressStatus.audioPlayerStatus) {
            AudioPlayerStatus.STATE_PLAYING -> pausePlayer()
            AudioPlayerStatus.STATE_PREPARED, AudioPlayerStatus.STATE_PAUSED -> startPlayer()
            AudioPlayerStatus.STATE_DEFAULT -> startPlayer()
            AudioPlayerStatus.STATE_ERROR -> showMassage()
        }
    }

    // Отображение сообщения
    private fun showMassage() {
        Toast.makeText(this, getString(R.string.audio_file_not_available), Toast.LENGTH_LONG).show()
    }
}
