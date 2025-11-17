package com.example.gaminghub.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gaminghub.data.model.Publicacion
import com.example.gaminghub.data.model.PublicacionCategory
import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.ui.navigation.ScreenSections
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.viewmodel.state.PublicacionUiState
import com.example.gaminghub.utils.Result
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PublicacionViewModel(
    private val repository: PublicacionRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = mutableStateOf(PublicacionUiState())
    val uiState: State<PublicacionUiState> = _uiState

    fun crearPublicacion(
        context: Context,
        titulo: String,
        contenido: String,
        tipo: String,
        imageUris: List<Uri> = emptyList()
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isSuccess = false,
                errorMessage = null,
                successMessage = null,
                shouldRedirect = false,
                redirectRoute = null
            )

            val userId = sessionManager.getUserId() ?: 0L

            val nuevaPublicacion = Publicacion(
                titulo = titulo,
                contenido = contenido,
                tipo = tipo,
                autorID = userId
            )

            val imageParts = imageUris.mapNotNull { uri ->
                val file = uriToFile(context, uri)
                file?.let {
                    val requestBody = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("imagenes", it.name, requestBody)
                }
            }

            when (val result = repository.crearPublicacion(nuevaPublicacion, imageParts)) {
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
                is Result.Success -> {
                    val route = if (tipo == PublicacionCategory.NOTICIA.name) {
                        ScreenSections.News.route
                    } else {
                        ScreenSections.Debates.route
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        successMessage = "Publicación creada correctamente",
                        shouldRedirect = true,
                        redirectRoute = route
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = result.message
                    )
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

    private fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("image_", ".jpg", context.cacheDir)
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
