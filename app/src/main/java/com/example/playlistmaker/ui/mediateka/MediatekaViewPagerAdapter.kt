package com.example.playlistmaker.ui.mediateka

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.mediateka.favoriteTracks.FavoriteTracksFragment
import com.example.playlistmaker.ui.mediateka.playlists.PlayListFragment

class MediatekaViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int {
        return FRAGMENTS_COUNT
    }

    // Создает и возвращает фрагмент в зависимости от его позиции
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteTracksFragment.newInstance()
            else -> PlayListFragment.newInstance()
        }
    }

    companion object {
        private const val FRAGMENTS_COUNT = 2
    }
}
