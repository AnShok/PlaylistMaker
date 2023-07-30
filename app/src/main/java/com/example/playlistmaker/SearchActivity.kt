package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
//import android.widget.LinearLayout

class SearchActivity : AppCompatActivity() {

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
    }

    private var textSearch: String = "" //глобальная переменная для хранения текста поискового запроса

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (savedInstanceState != null) {
            textSearch = savedInstanceState.getString(TEXT_SEARCH,"") //Восстановление значения textSearch из сохраненного состояния
        }

        //Нахождение элементов интерфейса
        val backButton = findViewById<Button>(R.id.button_back)
        //val linearLayout = findViewById<LinearLayout>(R.id.containerSearch)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        //Кнопка Назад - закрытие активити
        backButton.setOnClickListener {
            finish()
        }
        //Кнопка очистки поисковой строки
        clearButton.setOnClickListener {
            inputEditText.setText("")
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textSearch = s.toString() //Когда тект поискового запроса меняется, он сохраняется в переменную textSearch
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
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
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        textSearch = inputEditText.text.toString() // Сохранение значения текста поискового запроса в переменную
        outState.putString(TEXT_SEARCH, textSearch) //Сохранение значения textSearch в состояние активити
    }
}

