package com.wafflestudio.snuday.preference

import android.content.Context
import androidx.core.content.edit

class PreferenceHelper {

    companion object {
        private const val PREFERENCE_NAME = "SNUDAY"

        const val REFRESH_TOKEN = "refresh_token"
        const val ACCESS_TOKEN = "access_token"

        private const val DEFAULT_STRING_VALUE = ""
        private const val DEFAULT_BOOLEAN_VALUE = false



        fun getString(context: Context, key: String): String {
            val pref = getPreferences(context)

            return pref.getString(key, DEFAULT_STRING_VALUE)!!
        }

        fun setString(context: Context, key: String, value: String) {
            val pref = getPreferences(context)
            pref.edit {
                putString(key, value)
                apply()
            }
        }

        fun getBoolean(context: Context, key: String): Boolean {
            val pref = getPreferences(context)
            return pref.getBoolean(key, DEFAULT_BOOLEAN_VALUE)
        }

        fun setBoolean(context: Context, key: String, value: Boolean) {
            val pref = getPreferences(context)
            pref.edit {
                putBoolean(key, value)
                apply()
            }
        }

        private fun getPreferences(context: Context) =
            context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }


}