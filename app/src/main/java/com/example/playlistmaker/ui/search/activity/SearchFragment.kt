package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.model.SearchStatus
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.model.TrackSearchResult
import com.example.playlistmaker.ui.search.adapters.HistoryTracksAdapter
import com.example.playlistmaker.ui.search.adapters.SearchTracksAdapter
import com.example.playlistmaker.ui.search.view_model.TrackSearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModel<TrackSearchViewModel>()

    // Объявление адаптеров для результатов поиска и истории поиска
    private val searchAdapter = SearchTracksAdapter()
    private val historyAdapter = HistoryTracksAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.foundTracks.observe(viewLifecycleOwner) { it ->
            performSearching(it)
        }

        setupAdapters()
        setupEditText()
        setupButtons()

        // Загрузка истории поиска при создании
        loadSearchHistory()
        setupListenersAdapters()
        restoreState(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        saveSearchState()
    }

    override fun onResume() {
        super.onResume()
        restoreSearchState()
        if (viewModel.textSearch.isEmpty()) {
            binding.searchRecyclerView.visibility = View.GONE
            binding.searchHistoryLayout.visibility = if (historyAdapter.historyTracks.isEmpty()) View.GONE else View.VISIBLE
        } else {
            binding.searchRecyclerView.visibility = View.VISIBLE
            binding.searchHistoryLayout.visibility = View.GONE
        }
    }

    private fun setupAdapters() {
        binding.searchRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchRecyclerView.adapter = searchAdapter

        binding.historyRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun setupEditText() {
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.search()
                true
            } else {
                false
            }
        }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty() && viewModel.loadSearchHistory().isNotEmpty()) {
                binding.searchHistoryLayout.visibility = View.VISIBLE
                viewModel.loadSearchHistory()
                historyAdapter.notifyDataSetChanged()
            } else if (viewModel.loadSearchHistory().isNotEmpty()) {
                binding.searchHistoryLayout.visibility = View.VISIBLE
            } else {
                binding.searchHistoryLayout.visibility = View.GONE
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.textSearch = s.toString()
                binding.clearIcon.isVisible = !s.isNullOrEmpty()
                binding.searchHistoryLayout.visibility =
                    if (binding.inputEditText.hasFocus() && s?.isEmpty() == true && viewModel.loadSearchHistory().isNotEmpty()) View.VISIBLE
                    else View.GONE
                if (binding.inputEditText.text.toString().isEmpty()) {
                    hidePlaceholders()
                } else {
                    viewModel.changeTextSearch(binding.inputEditText.text.toString())
                    viewModel.search()
                    hidePlaceholders()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun setupButtons() {
        binding.clearIcon.setOnClickListener {
            clearSearch()
        }

        binding.clearSearchHistoryButton.setOnClickListener {
            historyAdapter.historyTracks.clear()
            viewModel.clearSearchHistory()
            historyAdapter.notifyDataSetChanged()
            binding.searchHistoryLayout.visibility = View.GONE
        }

        binding.refreshButton.setOnClickListener {
            if (viewModel.textSearch.isNotEmpty()) {
                binding.inputEditText.setText(viewModel.textSearch)
                viewModel.search()
            }
        }
    }

    private fun setupListenersAdapters() {
        searchAdapter.itemClickListener = object : SearchTracksAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                startAudioPlayer(track)
            }
        }

        historyAdapter.itemClickListener = object : HistoryTracksAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                startAudioPlayer(track)
            }
        }
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            viewModel.textSearch = savedInstanceState.getString(TEXT_SEARCH, "") ?: ""
            binding.inputEditText.setText(viewModel.textSearch)
            if (viewModel.textSearch.isNotEmpty()) {
                viewModel.search()
            }
        }
    }

    private fun startAudioPlayer(track: Track) {
        if (viewModel.clickDebounce()) {
            val bundle = Bundle()
            bundle.putParcelable(TRACK, track)
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayerFragment,
                bundle
            )
            viewModel.addToSearchHistory(track)
        }
    }

    private fun clearSearch() {
        binding.inputEditText.setText("")
        hideKeyboard()
        searchAdapter.searchTracks.clear()
        searchAdapter.notifyDataSetChanged()
        hidePlaceholders()
        binding.searchHistoryLayout.visibility = if (historyAdapter.historyTracks.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun loadSearchHistory() {
        val history = viewModel.loadSearchHistory()
        historyAdapter.historyTracks.clear()
        historyAdapter.historyTracks.addAll(history)
        historyAdapter.notifyDataSetChanged()
        binding.searchHistoryLayout.visibility = if (history.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
    }

    private fun hidePlaceholders() {
        binding.nothingFoundPlaceholder.visibility = View.GONE
        binding.errorPlaceholder.visibility = View.GONE
    }

    private fun showNothingFoundPlaceholder() {
        binding.searchHistoryLayout.visibility = View.GONE
        binding.nothingFoundPlaceholder.visibility = View.VISIBLE
        binding.errorPlaceholder.visibility = View.GONE
        binding.refreshButton.visibility = View.GONE
    }

    private fun showErrorPlaceholder() {
        binding.nothingFoundPlaceholder.visibility = View.GONE
        binding.errorPlaceholder.visibility = View.VISIBLE
        binding.refreshButton.visibility = View.VISIBLE
    }

    private fun performSearching(foundTracks: TrackSearchResult) {
        searchAdapter.searchTracks.clear()
        binding.searchHistoryLayout.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        when (foundTracks.resultStatus) {
            SearchStatus.RESPONSE_RECEIVED -> {
                binding.progressBar.visibility = View.GONE
                binding.searchRecyclerView.visibility = View.VISIBLE
                searchAdapter.searchTracks.addAll(foundTracks.tracks)
                searchAdapter.notifyDataSetChanged()
            }
            SearchStatus.NOTHING_FOUND -> {
                binding.progressBar.visibility = View.GONE
                showNothingFoundPlaceholder()
            }
            SearchStatus.NETWORK_ERROR -> {
                binding.progressBar.visibility = View.GONE
                showErrorPlaceholder()
            }
            SearchStatus.DEFAULT -> {
                binding.progressBar.visibility = View.GONE
            }
            SearchStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun saveSearchState() {
        viewModel.textSearch = binding.inputEditText.text.toString()
    }

    private fun restoreSearchState() {
        binding.inputEditText.setText(viewModel.textSearch)
        if (viewModel.textSearch.isNotEmpty()) {
            binding.searchHistoryLayout.visibility = View.GONE
            binding.searchRecyclerView.visibility = View.VISIBLE
        } else {
            binding.searchHistoryLayout.visibility = if (historyAdapter.historyTracks.isEmpty()) View.GONE else View.VISIBLE
            binding.searchRecyclerView.visibility = View.GONE
        }
    }

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
        const val TRACK = "track"
    }
}

