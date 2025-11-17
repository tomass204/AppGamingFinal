package com.example.gaminghub.ui.screen.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gaminghub.data.model.Publicacion
import com.example.gaminghub.data.network.repository.ImagenRepository
import com.example.gaminghub.utils.Result
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import com.google.accompanist.pager.*
import com.example.gaminghub.utils.toBitmap

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun PublicacionComentariosCard(
    publicacion: Publicacion,
    imagenRepository: ImagenRepository
) {
    val fechaFormateada = try {
        val fecha = LocalDateTime.parse(publicacion.fechaCreacion)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        fecha.format(formatter)
    } catch (e: Exception) {
        publicacion.fechaCreacion
    }

    val imagenesBitmap = remember { mutableStateListOf<ImageBitmap>() }
    val scope = rememberCoroutineScope()

    var showFullScreen by remember { mutableStateOf(false) }
    var startIndex by remember { mutableStateOf(0) }

    LaunchedEffect(publicacion.imagenes) {
        imagenesBitmap.clear()
        publicacion.imagenes?.forEach { imagen ->
            scope.launch {
                when (val resultado = imagenRepository.obtenerImagenPorId(imagen.imagenID)) {
                    is Result.Success -> imagenesBitmap.add(resultado.data.toBitmap())
                    else -> {}
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text(
            publicacion.titulo ?: "Sin título",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            "Por: ${publicacion.autor} El: $fechaFormateada",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Spacer(Modifier.height(4.dp))
        Text(
            publicacion.contenido ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (imagenesBitmap.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            BoxWithConstraints {
                val totalWidth = maxWidth
                val spacing = 8.dp
                val visibleCount = 4
                val imagenWidth: Dp = (totalWidth - spacing * (visibleCount - 1)) / visibleCount
                val listState = rememberLazyListState()

                Box {
                    LazyRow(
                        state = listState,
                        horizontalArrangement = Arrangement.spacedBy(spacing),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(imagenesBitmap) { index, img ->
                            Image(
                                bitmap = img,
                                contentDescription = "Imagen de publicación",
                                modifier = Modifier
                                    .width(imagenWidth)
                                    .height(180.dp)
                                    .clickable {
                                        startIndex = index
                                        showFullScreen = true
                                    }
                            )
                        }
                    }

                    if (imagenesBitmap.size > visibleCount) {
                        val arrowBg = Color(0x66000000)

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(40.dp)
                                .background(arrowBg, shape = MaterialTheme.shapes.small)
                                .clickable {
                                    scope.launch {
                                        val target = (listState.firstVisibleItemIndex - 1).coerceAtLeast(0)
                                        listState.animateScrollToItem(target)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "<",
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(40.dp)
                                .background(arrowBg, shape = MaterialTheme.shapes.small)
                                .clickable {
                                    scope.launch {
                                        val target = (listState.firstVisibleItemIndex + 1)
                                            .coerceAtMost(imagenesBitmap.size - 1)
                                        listState.animateScrollToItem(target)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                ">",
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    if (showFullScreen) {
        Dialog(onDismissRequest = { /* No se cierra tocando afuera */ }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)) // Fondo celeste
            ) {
                val pagerState = rememberPagerState(initialPage = startIndex)

                HorizontalPager(
                    count = imagenesBitmap.size,
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    Image(
                        bitmap = imagenesBitmap[page],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    activeColor = MaterialTheme.colorScheme.primary,
                    inactiveColor = Color.Gray
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), shape = MaterialTheme.shapes.small)
                        .clickable { showFullScreen = false },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "X",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
