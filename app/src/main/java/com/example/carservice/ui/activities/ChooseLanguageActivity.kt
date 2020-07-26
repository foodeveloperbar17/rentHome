package com.example.carservice.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.carservice.R
import com.example.carservice.utils.LanguageUtils
import java.util.*

class ChooseLanguageActivity : AppCompatActivity() {

    private lateinit var georgiaImageView: ImageView
    private lateinit var englishImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_language)

        val sharedPreferences = getSharedPreferences(LanguageUtils.LANGUAGE_PREFERENCES_KEY, Context.MODE_PRIVATE)
        if (sharedPreferences.contains(LanguageUtils.LANGUAGE_KEY)) {
            startMainActivity()
            return
        }
        initUI()
        initUIActions()
    }

    private fun initUI() {
        georgiaImageView = findViewById(R.id.georgian_language_image)
        englishImageView = findViewById(R.id.english_language_image)
    }

    private fun initUIActions() {
        georgiaImageView.setOnClickListener {
            setLanguage(LanguageUtils.GEORGIAN_LANGUAGE_CODE)
        }

        englishImageView.setOnClickListener {
            setLanguage(LanguageUtils.ENGLISH_LANGUAGE_CODE)
        }
    }

    private fun setLanguage(lang: String) {
        LanguageUtils.setLanguage(lang, this)

        startMainActivity()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}
