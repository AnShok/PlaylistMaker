package com.example.playlistmaker

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Utils.formatTrackDuration

class AudioPlayerActivity : AppCompatActivity() {
    private var trackName: String? = null
    private var artistName: String? = null
    private var trackTimeMillis: Long = 0
    private var collectionName: String? = null
    private var releaseDate: String? = null
    private var primaryGenreName: String? = null
    private var country: String? = null
    private var artworkUrl100: String? = null
    private var trackDuration: String? = null
    private var isDayTheme: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        // Восстановление данных, если они были сохранены в onSaveInstanceState
        if (savedInstanceState != null) {
            trackName = savedInstanceState.getString("trackName")
            artistName = savedInstanceState.getString("artistName")
            trackTimeMillis = savedInstanceState.getLong("trackTimeMillis")
            collectionName = savedInstanceState.getString("collectionName")
            releaseDate = savedInstanceState.getString("releaseDate")
            primaryGenreName = savedInstanceState.getString("primaryGenreName")
            country = savedInstanceState.getString("country")
            artworkUrl100 = savedInstanceState.getString("artworkUrl100")
            trackDuration = savedInstanceState.getString("trackDuration")
        } else {
            // Получаем данные о треке, переданные из предыдущей активности
            trackName = intent.getStringExtra("trackName")
            artistName = intent.getStringExtra("artistName")
            trackTimeMillis = intent.getLongExtra("trackTimeMillis", 0)
            collectionName = intent.getStringExtra("collectionName")
            releaseDate = intent.getStringExtra("releaseDate")
            primaryGenreName = intent.getStringExtra("primaryGenreName")
            country = intent.getStringExtra("country")
            artworkUrl100 = intent.getStringExtra("artworkUrl100")
            trackDuration = intent.getStringExtra("trackDuration")
        }

        // Отображение данных о треке на экране
        val trackNameTextView: TextView = findViewById(R.id.track_name)
        val artistNameTextView: TextView = findViewById(R.id.artist_name)
        val trackTimeMillisTextView: TextView = findViewById(R.id.track_time_milles)
        val collectionNameTextView: TextView = findViewById(R.id.album_name)
        val releaseDateTextView: TextView = findViewById(R.id.release_year_name)
        val primaryGenreTextView: TextView = findViewById(R.id.genre_name)
        val countryTextView: TextView = findViewById(R.id.country_name)
        val coverImageView: ImageView = findViewById(R.id.cover_image)

        trackNameTextView.text = trackName
        artistNameTextView.text = artistName
        trackTimeMillisTextView.text = formatTrackDuration(trackTimeMillis)
        collectionNameTextView.text = collectionName
        releaseDateTextView.text = releaseDate
        primaryGenreTextView.text = primaryGenreName
        countryTextView.text = country

        // улучшение качества обложк
        val artworkUrl512 = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
        //Радиус в dp
        val cornerRadiusDp = 8f
        //Преобразование из dp в px
        val cornerRadiusPx = dpToPx(cornerRadiusDp, this)
        // загрузка обложки
        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.placeholder) // Заглушка, если изображение не загружено
            .transform(RoundedCorners(cornerRadiusPx)) // Применение скругленных углов
            .into(coverImageView)

        if (trackDuration != null) {
            trackTimeMillisTextView.text = trackDuration
        }

        //aорматирование даты релиза в гггг
        if (releaseDate != null && releaseDate!!.length >= 4) {
            val year = releaseDate!!.substring(0, 4)
            releaseDateTextView.text = year
        } else {
            releaseDateTextView.text = ""
        }


        val backButton = findViewById<Button>(R.id.back_button)
        //Кнопка Назад - закрытие активити
        backButton.setOnClickListener {
            finish()
        }

        //Определение текущей темы день/ночь
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        isDayTheme = currentNightMode == Configuration.UI_MODE_NIGHT_NO
        setTheme()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Сохранение данных в случае уничтожения активити
        super.onSaveInstanceState(outState)
        outState.putString("trackName", trackName)
        outState.putString("artistName", artistName)
        outState.putLong("trackTimeMillis", trackTimeMillis)
        outState.putString("collectionName", collectionName)
        outState.putString("releaseDate", releaseDate)
        outState.putString("primaryGenreName", primaryGenreName)
        outState.putString("country", country)
        outState.putString("artworkUrl100", artworkUrl100)
        outState.putString("trackDuration", trackDuration)
        outState.putBoolean(IS_DAY_THEME, isDayTheme)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

    private fun setTheme() {
        if (isDayTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            //Подстановка изображения для дневной темы
            findViewById<ImageView>(R.id.add_to_playlist_button).setImageResource(R.drawable.add_to_playlist_button_day)
            findViewById<ImageView>(R.id.play_pause_button).setImageResource(R.drawable.play_pause_button_day)
            findViewById<ImageView>(R.id.add_to_favorite_button).setImageResource(R.drawable.add_to_favorite_button_day)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            //Подстановка изображения для ночной темы
            findViewById<ImageView>(R.id.add_to_playlist_button).setImageResource(R.drawable.add_to_playlist_button_night)
            findViewById<ImageView>(R.id.play_pause_button).setImageResource(R.drawable.play_pause_button_night)
            findViewById<ImageView>(R.id.add_to_favorite_button).setImageResource(R.drawable.add_to_favorite_button_night)
        }
    }

    companion object {
        const val IS_DAY_THEME = "IS_DAY_THEME"
    }
}