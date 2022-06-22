package me.daffakurnia.android.storyapp.ui

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import me.daffakurnia.android.storyapp.Injection
import me.daffakurnia.android.storyapp.data.StoriesRepository
import me.daffakurnia.android.storyapp.response.StoriesResponseItem

class MainViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {

    private val _stories = MutableLiveData<PagingData<StoriesResponseItem>>()

    fun stories(token: String): LiveData<PagingData<StoriesResponseItem>> {
        val response = storiesRepository.getStories(token).cachedIn(viewModelScope)
        _stories.value = response.value
        return response
    }

}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}