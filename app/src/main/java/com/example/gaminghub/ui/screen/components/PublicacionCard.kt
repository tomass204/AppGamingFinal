package com.example.gaminghub.ui.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gaminghub.R
import com.example.gaminghub.data.model.Publicacion
import com.example.gaminghub.data.network.repository.ImagenRepository
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import com.example.gaminghub.utils.Result
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.gaminghub.utils.toBitmap

@Composable
fun PublicacionCard(
    publicacion: Publicacion,
    imagenRepository: ImagenRepository,
    onLikeClick: (likeID: Long?) -> Unit,
    onCommentClick: () -> Unit,
    onFavoriteClick: (favoritoID: Long?) -> Unit
) {
    val fechaFormateada = try {
        val fecha = LocalDateTime.parse(publicacion.fechaCreacion)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        fecha.format(formatter)
    } catch (e: Exception) {
        publicacion.fechaCreacion
    }

    val scope = rememberCoroutineScope()
    var bitmapState by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(publicacion.imagenes) {
        publicacion.imagenes?.firstOrNull()?.let { imagen ->
            when (val resultado = imagenRepository.obtenerImagenPorId(imagen.imagenID)) {
                is Result.Success -> bitmapState = resultado.data.toBitmap()
                else -> {}
            }
        }
    }

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray)
                    .padding(4.dp)
            ) {
                when {
                    bitmapState != null -> {
                        Image(
                            bitmap = bitmapState!!,
                            contentDescription = publicacion.titulo,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White),
                            contentScale = ContentScale.Crop
                        )
                    }
                    publicacion.imageUri != null -> {
                        AsyncImage(
                            model = publicacion.imageUri,
                            contentDescription = publicacion.titulo,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White),
                            contentScale = ContentScale.Crop
                        )
                    }
                    publicacion.imageResId != null && publicacion.imageResId != 0 -> {
                        Image(
                            painter = painterResource(id = publicacion.imageResId),
                            contentDescription = publicacion.titulo,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else -> {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        publicacion.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Text(
                        "Por: ${publicacion.autor} El: $fechaFormateada",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        publicacion.contenido,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onLikeClick(publicacion.likeID) }) {
                            Icon(
                                if (publicacion.isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                contentDescription = "Like",
                                tint = if (publicacion.isLiked) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            "${publicacion.likesCount}",
                            color = if (publicacion.isLiked) MaterialTheme.colorScheme.primary else Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onCommentClick) {
                            Icon(
                                Icons.Outlined.Comment,
                                contentDescription = "Comment",
                                tint = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            "${publicacion.comentariosCount}",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    IconButton(onClick = { onFavoriteClick(publicacion.favoritoID) }) {
                        Icon(
                            if (publicacion.isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (publicacion.isFavorited) Color.Red else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
