package com.example.carservice.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.carservice.R
import java.util.*

class ChooseLanguageActivity : AppCompatActivity() {

    companion object {
        const val LANGUAGE_PREFERENCES_KEY = "language"
        const val LANGUAGE_KEY = "lang"

        const val GEORGIAN_LANGUAGE_CODE = "ka_"
        const val ENGLISH_LANGUAGE_CODE = "en_US"

    }

    private lateinit var georgiaImageView: ImageView
    private lateinit var englishImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_language)

        val sharedPreferences = getSharedPreferences(LANGUAGE_PREFERENCES_KEY, Context.MODE_PRIVATE)
        if (sharedPreferences.contains(LANGUAGE_KEY)) {
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
            setLanguage(GEORGIAN_LANGUAGE_CODE)
        }

        englishImageView.setOnClickListener {
            setLanguage(ENGLISH_LANGUAGE_CODE)
        }
    }

    private fun setLanguage(lang: String) {
        val sharedPreferences = getSharedPreferences(LANGUAGE_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(LANGUAGE_KEY, lang)
        editor.apply()

        val myLocale = Locale(lang)

        val dm = resources.displayMetrics

        val conf = resources.configuration

        conf.setLocale(myLocale)
        resources.updateConfiguration(conf, dm)

        startMainActivity()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}
