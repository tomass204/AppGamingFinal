package com.example.gaminghub.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gaminghub.data.network.repository.FavoritoRepository
import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.data.network.repository.ReaccionRepository
import com.example.gaminghub.ui.navigation.SessionManager

class DebateViewModelFactory(
    private val repository: PublicacionRepository,
    private val sessionManager: SessionManager,
    private val reaccionRepository: ReaccionRepository,
    private val favoritoRepository: FavoritoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DebateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DebateViewModel(
                repository,
                sessionManager,
                reaccionRepository,
                favoritoRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
