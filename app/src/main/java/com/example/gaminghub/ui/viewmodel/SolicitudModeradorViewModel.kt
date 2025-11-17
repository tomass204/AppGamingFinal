package com.example.gaminghub.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gaminghub.data.model.SolicitudModerador
import com.example.gaminghub.data.network.repository.SolicitudModeradorRepository
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.viewmodel.state.SolicitudUiState
import com.example.gaminghub.utils.Result
import kotlinx.coroutines.launch

class SolicitudModeradorViewModel(
    private val repository: SolicitudModeradorRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = mutableStateOf(SolicitudUiState())
    val uiState: State<SolicitudUiState> = _uiState

    fun loadPendingRequests() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = repository.listarSolicitudes()) {
                is Result.Success -> {
                    val pendientes = result.data.filter { it.estado.equals("PENDIENTE", ignoreCase = true) }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        solicitudes = pendientes,
                        isSuccess = true
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun processApproval(
        solicitudModerador: SolicitudModerador,
        sessionManager: SessionManager
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = repository.aprobarSolicitud(solicitudModerador.solicitudId ?: return@launch)) {
                is Result.Success -> {
                    val nuevasSolicitudes = _uiState.value.solicitudes.filter {
                        it.solicitudId != solicitudModerador.solicitudId
                    }
                    _uiState.value = _uiState.value.copy(
                        solicitudes = nuevasSolicitudes,
                        isLoading = false,
                        successMessage = "Solicitud aprobada correctamente."
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun rejectRequest(
        solicitudModerador: SolicitudModerador,
        sessionManager: SessionManager
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = repository.rechazarSolicitud(solicitudModerador.solicitudId ?: return@launch)) {
                is Result.Success -> {
                    val nuevasSolicitudes = _uiState.value.solicitudes.filter {
                        it.solicitudId != solicitudModerador.solicitudId
                    }
                    _uiState.value = _uiState.value.copy(
                        solicitudes = nuevasSolicitudes,
                        isLoading = false,
                        successMessage = "Solicitud rechazada correctamente."
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }


    fun crearSolicitud(
        sessionManager: SessionManager,
        motivo: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val solicitudModerador = SolicitudModerador(
                usuarioId = sessionManager.getUserId() ?: return@launch,
                motivo = motivo,
                email = sessionManager.getUserEmail() ?: return@launch,
                nombre = sessionManager.getUserName() ?: return@launch
            )

            when (val result = repository.crearSolicitud(solicitudModerador ?: return@launch)) {
                is Result.Success -> {
                    val nuevasSolicitudes = _uiState.value.solicitudes.filter {
                        it.solicitudId != solicitudModerador.solicitudId
                    }
                    _uiState.value = _uiState.value.copy(
                        solicitudes = nuevasSolicitudes,
                        isLoading = false,
                        successMessage = "Solicitud creada correctamente."
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }




}
