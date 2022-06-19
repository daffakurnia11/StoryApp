package me.daffakurnia.android.storyapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import me.daffakurnia.android.storyapp.api.ApiConfig
import me.daffakurnia.android.storyapp.data.StoriesRepository

object Injection {

    fun provideRepository(token: String, context: Context): StoriesRepository {
        val apiService = ApiConfig.getApiService()
        return StoriesRepository(token, apiService)
    }
}