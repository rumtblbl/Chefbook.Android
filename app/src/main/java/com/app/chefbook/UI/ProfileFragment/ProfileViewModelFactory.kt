package com.app.chefbook.UI.ProfileFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.chefbook.Data.DataManager

@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory (val dataManager: DataManager) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(dataManager) as T
    }
}