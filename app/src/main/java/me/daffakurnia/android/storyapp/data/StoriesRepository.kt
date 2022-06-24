package me.daffakurnia.android.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import me.daffakurnia.android.storyapp.api.ApiService
import me.daffakurnia.android.storyapp.response.StoriesResponseItem

class StoriesRepository(
    private val storiesDatabase: StoriesDatabase,
    private val apiService: ApiService
) {
    fun getStories(token: String): LiveData<PagingData<StoriesResponseItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoriesPagingSource(token, apiService)
            }
        ).liveData
    }
}