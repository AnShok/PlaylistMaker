package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioplayerBinding
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var playbackProgressHandler: Handler? = null
    private var currentTrack: Track? = null
    private var currentTimeText: TextView? = null
    private var playPauseButton: ImageButton? = null
    private var currentPosition: Int = 0
    private val updateProgressHandler = Handler(Looper.getMainLooper())

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            updatePlaybackProgress()
        }
    }

    private fun updatePlaybackProgress() {
        if (playerState == STATE_PLAYING) {
            currentPosition = mediaPlayer.currentPosition
            val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
            currentTimeText?.text = formattedTime

            updateProgressHandler.postDelayed(updateProgressRunnable, 300)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        currentTimeText = binding.trackPlaybackTime
        playPauseButton = binding.playPauseButton

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("track", Track::class.java)
        } else {
            intent.getParcelableExtra("track")
        }

        currentTrack = track

        if (track != null) {
            bindingTrackDataInActivity(track)
        }

        preparePlayer()

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        playbackProgressHandler?.removeCallbacks(updateProgressRunnable)
    }

    // Привязка вью к данным
    private fun bindingTrackDataInActivity(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTimeMillis.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.albumName.text = track.collectionName ?: ""

        // Форматирование даты релиза
        val formattedReleaseDate =
            if (!TextUtils.isEmpty(track.releaseDate) && track.releaseDate.length >= 4) {
                track.releaseDate.substring(0, 4)
            } else {
                ""
            }
        binding.releaseYearName.text = formattedReleaseDate

        binding.genreName.text = track.primaryGenreName
        binding.countryName.text = track.country

        // Улучшение качества обложки
        val artworkUrl512 = track.getArtworkUrl512()
        //Скругление обложки
        val cornerRadiusDp = 8f
        val cornerRadiusPx = dpToPx(cornerRadiusDp)

        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(cornerRadiusPx))
            .into(binding.coverImage)

        binding.playPauseButton.setOnClickListener {
            playbackControl()
        }
    }

    private fun dpToPx(dp: Float): Int {
        val density = resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }

    private fun preparePlayer() {
        val url = currentTrack?.previewUrl
        url?.let {
            mediaPlayer.setDataSource(it)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playPauseButton?.isEnabled = true
                playerState = STATE_PREPARED
                currentPosition = 0

                if (playerState == STATE_PLAYING) {
                    updateProgressHandler.post(updateProgressRunnable)
                }

                val duration = mediaPlayer.duration
                val formattedDuration = SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)

            }
            mediaPlayer.setOnCompletionListener {
                playPauseButton?.setImageResource(R.drawable.play_button_day)
                playerState = STATE_PREPARED
                currentTimeText?.text = "00:00"
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        updateProgressHandler.post(updateProgressRunnable)
        playPauseButton?.setImageResource(R.drawable.pause_button_day)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        updateProgressHandler.removeCallbacks(updateProgressRunnable)
        playPauseButton?.setImageResource(R.drawable.play_button_day)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
}