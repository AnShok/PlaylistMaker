package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class SearchActivity : AppCompatActivity() {

    private companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
    }

    private lateinit var inputEditText: EditText //глобальная переменная для EditText
    private lateinit var clearButton: ImageView
    private lateinit var recyclerView: RecyclerView

    private var textSearch: String =
        "" //глобальная переменная для хранения текста поискового запроса


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Нахождение элементов интерфейса
        inputEditText = findViewById(R.id.inputEditText) // инициализация inputEditText в onCreate
        clearButton = findViewById(R.id.clearIcon)
        recyclerView = findViewById(R.id.recycler_view)
        //val linearLayout = findViewById<LinearLayout>(R.id.containerSearch)

        val backButton = findViewById<Button>(R.id.button_back)

        if (savedInstanceState != null) {
            textSearch = savedInstanceState.getString(
                TEXT_SEARCH,
                ""
            ) //Восстановление значения textSearch из сохраненного состояния
            inputEditText.setText(textSearch) //Восстановление текст в EditText из сохраненного состояния
        }

        //Кнопка Назад - закрытие активити
        backButton.setOnClickListener {
            finish()
        }
        //Кнопка очистки поисковой строки
        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textSearch =
                    s.toString() //Когда тект поискового запроса меняется, он сохраняется в переменную textSearch
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val trackList = TrackData.generateTrackData()
        val adapter = TrackAdapter(trackList)
        recyclerView.adapter = adapter
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        textSearch =
            inputEditText.text.toString() // Сохранение значения текста поискового запроса в переменную
        outState.putString(
            TEXT_SEARCH,
            textSearch
        ) //Сохранение значения textSearch в состояние активити
    }

    //Функция скрытия клавиатуры после очистки
    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
    }
}
object TrackData {

    private const val IMAGE_URL_1 = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
    private const val IMAGE_URL_2 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
    private const val IMAGE_URL_3 = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
    private const val IMAGE_URL_4 = "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
    private const val IMAGE_URL_5 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"

    fun generateTrackData(): List<Track> {
        return listOf(
            Track("Smells Like Teen Spirit", "Nirvana", "5:01", IMAGE_URL_1),
            Track("Billie Jean", "Michael Jackson", "4:35", IMAGE_URL_2),
            Track("Stayin' Alive", "Bee Gees", "4:10", IMAGE_URL_3),
            Track("Whole Lotta Love", "Led Zeppelin", "5:33", IMAGE_URL_4),
            Track("Sweet Child O'Mine", "Guns N' Roses", "5:03", IMAGE_URL_5)
        )
    }
}

