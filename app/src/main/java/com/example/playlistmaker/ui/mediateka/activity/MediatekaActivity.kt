package com.example.playlistmaker.ui.mediateka.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatekaBinding
import com.example.playlistmaker.ui.mediateka.adapter.MediatekaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediatekaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediatekaBinding

    // Объект для связывания вкладок и ViewPager в медиатеке
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediatekaViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator =
            TabLayoutMediator(binding.tabLayoutMediateka, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = resources.getString(R.string.favorite_tracks)
                    else -> tab.text = resources.getString(R.string.playlists)
                }
            }
        tabMediator.attach()

        //Кнопка Назад - закрытие активити
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}