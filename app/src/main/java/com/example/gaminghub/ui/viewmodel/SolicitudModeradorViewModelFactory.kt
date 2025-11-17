package com.example.gaminghub.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gaminghub.data.network.repository.SolicitudModeradorRepository
import com.example.gaminghub.ui.navigation.SessionManager


class SolicitudModeradorViewModelFactory(
    private val repository: SolicitudModeradorRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SolicitudModeradorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SolicitudModeradorViewModel(
                repository,
                sessionManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}