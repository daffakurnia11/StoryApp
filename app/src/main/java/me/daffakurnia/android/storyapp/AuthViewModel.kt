package me.daffakurnia.android.storyapp

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class AuthViewModel(private val pref: AppDataStore) : ViewModel() {
    fun loginToken(): LiveData<String?> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun clearToken() {
        viewModelScope.launch {
            pref.clearToken()
        }
    }
}