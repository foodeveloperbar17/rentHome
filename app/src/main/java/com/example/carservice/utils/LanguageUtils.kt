package com.example.carservice.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.util.*

object LanguageUtils {

    const val LANGUAGE_PREFERENCES_KEY = "language"
    const val LANGUAGE_KEY = "lang"

    const val GEORGIAN_LANGUAGE_CODE = "ka_"
    const val ENGLISH_LANGUAGE_CODE = "en_US"

    fun setLanguage(lang: String, activity: AppCompatActivity){
        val sharedPreferences = activity.getSharedPreferences(LanguageUtils.LANGUAGE_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(LanguageUtils.LANGUAGE_KEY, lang)
        editor.apply()

        val myLocale = Locale(lang)

        val dm = activity.resources.displayMetrics

        val conf = activity.resources.configuration

        conf.setLocale(myLocale)
        activity.resources.updateConfiguration(conf, dm)
    }
}