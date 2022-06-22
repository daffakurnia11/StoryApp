package me.daffakurnia.android.storyapp

import android.content.Context
import me.daffakurnia.android.storyapp.api.ApiConfig
import me.daffakurnia.android.storyapp.data.StoriesRepository

object Injection {

    fun provideRepository(context: Context): StoriesRepository {
        val apiService = ApiConfig.getApiService()
        return StoriesRepository(apiService)
    }
}