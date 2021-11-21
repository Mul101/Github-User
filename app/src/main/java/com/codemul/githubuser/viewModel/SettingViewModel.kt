package com.codemul.githubuser.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.codemul.githubuser.repository.SettingPreferences
//import com.codemul.githubuser.repository.SettingPreferences
import kotlinx.coroutines.launch

//Di sini kita akan mengubah data stream berupa Flow/Flowable menjadi LiveData
class SettingViewModel(private val pref: SettingPreferences) : ViewModel() {

    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

}