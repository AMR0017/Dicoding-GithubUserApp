package com.example.githubappsub2.ui.view

import androidx.lifecycle.*
import com.example.githubappsub2.ui.theme.SettingPreference
import kotlinx.coroutines.launch

class ThemeSettingView (private val pref: SettingPreference) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}

