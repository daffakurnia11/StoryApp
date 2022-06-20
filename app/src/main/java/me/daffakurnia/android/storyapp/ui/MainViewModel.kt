package me.daffakurnia.android.storyapp.ui

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import me.daffakurnia.android.storyapp.Injection
import me.daffakurnia.android.storyapp.data.StoriesRepository
import me.daffakurnia.android.storyapp.response.ListStoryItem

class MainViewModel(storiesRepository: StoriesRepository) : ViewModel() {
    val stories: LiveData<PagingData<ListStoryItem>> =
        storiesRepository.getQuote().cachedIn(viewModelScope)
}

class ViewModelFactory(private val token: String, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(token, context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}