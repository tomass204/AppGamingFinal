package com.example.gaminghub.ui.viewmodel

import ComentarioViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gaminghub.data.network.repository.ComentarioRepository
import com.example.gaminghub.data.network.repository.ImagenRepository
import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.ui.navigation.SessionManager

class ComentarioViewModelFactory(
    private val publicacionId: Long,
    private val sessionManager: SessionManager,
    private val repository: ComentarioRepository,
    private val publicacionRepository: PublicacionRepository,
    private val imagenRepository: ImagenRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComentarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ComentarioViewModel(
                publicacionId = publicacionId,
                sessionManager = sessionManager,
                repository = repository,
                publicacionRepository = publicacionRepository,
                imagenRepository = imagenRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}