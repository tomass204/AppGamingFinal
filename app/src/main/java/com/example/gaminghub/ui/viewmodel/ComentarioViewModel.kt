import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gaminghub.data.model.Comentario
import com.example.gaminghub.data.network.repository.ComentarioRepository
import com.example.gaminghub.data.network.repository.ImagenRepository
import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.viewmodel.state.ComentarioUiState
import com.example.gaminghub.utils.Result
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ComentarioViewModel(
    private val publicacionId: Long,
    private val sessionManager: SessionManager,
    private val repository: ComentarioRepository,
    private val publicacionRepository: PublicacionRepository,
    private val imagenRepository: ImagenRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(ComentarioUiState())
    val uiState: State<ComentarioUiState> = _uiState

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun cargarComentarios() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                // Cargar publicación (Result)
                val resultadoPublicacion = publicacionRepository.verPublicacion(publicacionId)

                when (resultadoPublicacion) {
                    is Result.Success -> {
                        val publicacion = resultadoPublicacion.data

                        // Cargar comentarios
                        when (val resultadoComentarios = repository.listarComentariosPorPublicacion(publicacionId)) {
                            is Result.Success -> {
                                _uiState.value = _uiState.value.copy(
                                    publicacion = publicacion,
                                    comentarios = resultadoComentarios.data,
                                    isLoading = false,
                                    errorMessage = null
                                )
                            }

                            is Result.Error -> {
                                _uiState.value = _uiState.value.copy(
                                    publicacion = publicacion,
                                    isLoading = false,
                                    errorMessage = resultadoComentarios.message
                                )
                            }

                            else -> {
                                _uiState.value = _uiState.value.copy(
                                    publicacion = publicacion,
                                    isLoading = false
                                )
                            }
                        }
                    }

                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = resultadoPublicacion.message
                        )
                    }

                    else -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar datos: ${e.message}"
                )
            }
        }
    }


    fun agregarComentario(texto: String) {
        val contenido = texto.trim()
        if (contenido.isEmpty()) {
            _uiState.value = _uiState.value.copy(errorMessage = "El comentario no puede estar vacío")
            return
        }

        val usuarioId = sessionManager.getUserId() ?: 0L
        if (usuarioId == 0L) {
            _uiState.value = _uiState.value.copy(errorMessage = "Usuario no autenticado")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val nuevoComentario = Comentario(
                contenido = contenido,
                publicacionID = publicacionId,
                autorID = usuarioId,
                fechaCreacion = dateFormat.format(Date()),
                activo = true
            )

            try {
                when (val result = repository.crearComentario(nuevoComentario)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            successMessage = "Comentario publicado correctamente",
                            isLoading = false
                        )
                        cargarComentarios()
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                    else -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al publicar el comentario: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun eliminarComentario(comentarioId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                when (val result = repository.eliminarComentario(comentarioId)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            successMessage = "Comentario eliminado correctamente",
                            isLoading = false
                        )
                        cargarComentarios()
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                    else -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al eliminar el comentario: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }
}
