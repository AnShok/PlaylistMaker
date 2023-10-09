package com.example.playlistmaker

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioplayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("track", Track::class.java)
        } else {
            intent.getParcelableExtra("track")
        }


        if (track != null) {
            bindingTrackDataInActivity(track)
        }


    }


    // Привязка вью к данным
    private fun bindingTrackDataInActivity(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(track.trackTimeMillis)
        binding.albumName.text = track.collectionName ?: ""

        // Форматирование даты релиза
        val formattedReleaseDate = if (!TextUtils.isEmpty(track.releaseDate) && track.releaseDate.length >= 4) {
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

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun dpToPx(dp: Float): Int {
        val density = resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }
}