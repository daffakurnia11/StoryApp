package me.daffakurnia.android.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import me.daffakurnia.android.storyapp.api.ApiService
import me.daffakurnia.android.storyapp.response.ListStoryItem

class StoriesRepository(val token: String, private val apiService: ApiService) {
    fun getQuote(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                initialLoadSize = 5
            ),
            pagingSourceFactory = {
                StoriesPagingSource(token, apiService)
            }
        ).liveData
    }
}