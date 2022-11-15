package com.example.githubappsub2.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubappsub2.data.UserRepository
import com.example.githubappsub2.ui.detail.User
import com.example.githubappsub2.ui.theme.SettingPreference
import java.lang.IllegalArgumentException

class ViewModelFactory(private val pref : SettingPreference) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeSettingView::class.java)){
            return ThemeSettingView(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class : " + modelClass.name )
    }
}