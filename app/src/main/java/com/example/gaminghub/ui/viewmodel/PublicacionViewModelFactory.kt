package com.example.gaminghub.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.ui.navigation.SessionManager

class PublicacionViewModelFactory(
    private val repository: PublicacionRepository,
    private val sessionManager: SessionManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PublicacionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PublicacionViewModel(
                repository,
                sessionManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
