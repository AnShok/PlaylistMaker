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
//import android.widget.LinearLayout

class SearchActivity : AppCompatActivity() {

    private companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
    }

    private lateinit var inputEditText: EditText //глобальная переменная для EditText
    private var textSearch: String = "" //глобальная переменная для хранения текста поискового запроса


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Нахождение элементов интерфейса
        inputEditText = findViewById(R.id.inputEditText) // инициализация inputEditText в onCreate
        val backButton = findViewById<Button>(R.id.button_back)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        //val linearLayout = findViewById<LinearLayout>(R.id.containerSearch)


        if (savedInstanceState != null) {
            textSearch = savedInstanceState.getString(TEXT_SEARCH,"") //Восстановление значения textSearch из сохраненного состояния
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
        textSearch = inputEditText.text.toString() // Сохранение значения текста поискового запроса в переменную
        outState.putString(TEXT_SEARCH, textSearch) //Сохранение значения textSearch в состояние активити
    }

    //Функция скрытия клавиатуры после очистки
    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
    }
}

