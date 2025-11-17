package com.example.gaminghub.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gaminghub.data.model.UserRole
import com.example.gaminghub.di.AppContainer
import com.example.gaminghub.ui.components.ConfirmDialog
import com.example.gaminghub.ui.navigation.Routes
import com.example.gaminghub.ui.navigation.ScreenSections
import com.example.gaminghub.ui.screen.drawer.getDrawerItems
import com.example.gaminghub.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import com.example.gaminghub.utils.Result

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreenWithDrawer(
    appNavController: NavController,
    authViewModel: AuthViewModel,
    appContainer: AppContainer
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val contentNavController = rememberNavController()
    val uiState by authViewModel.uiState
    val currentUserRole = appContainer.sessionManager.getUserRole()


    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.loggedInUserId) {
        if (uiState.loggedInUserId == null) {
            appNavController.navigate(Routes.AUTH_SCREEN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    val drawerItems = getDrawerItems(currentUserRole)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(24.dp))
                Text(
                    "GamingHub Menú",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Divider(Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

                drawerItems.forEach { screen ->
                    val navBackStackEntry =
                        contentNavController.currentBackStackEntryAsState().value
                    val currentRoute = navBackStackEntry?.destination?.route
                    NavigationDrawerItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            if (screen == ScreenSections.Logout) {
                                showLogoutDialog = true
                            } else {
                                contentNavController.navigate(screen.route) {
                                    popUpTo(contentNavController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = false
                                }
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                val navBackStackEntry = contentNavController.currentBackStackEntryAsState().value
                val currentRoute = navBackStackEntry?.destination?.route
                val screenTitle =
                    ScreenSections.values().find { it.route == currentRoute }?.title ?: "GamingHub"

                TopAppBar(
                    title = { Text(screenTitle) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF29B6F6),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            },
            floatingActionButton = {
                if (currentUserRole == UserRole.CREADOR_DE_CONTENIDO.name) {
                    FloatingActionButton(
                        onClick = { contentNavController.navigate(Routes.CREATE_CONTENT_SCREEN) },
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(bottom = 80.dp, end = 16.dp) // mueve el FAB hacia arriba y un poco a la derecha
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Crear Contenido",
                            tint = Color.White
                        )
                    }
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = contentNavController,
                startDestination = ScreenSections.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(ScreenSections.Home.route) {
                    PermanentWelcomeScreen(appContainer.sessionManager)
                }

                composable(ScreenSections.Perfil.route) {
                    PerfilScreen(
                        sessionManager = appContainer.sessionManager,
                        solicitudModeradorRepository = appContainer.solicitudRepository,
                        usuarioRepository = appContainer.usuarioRepository,
                        onLogout = { authViewModel.logout() }
                    )
                }

                composable(ScreenSections.News.route) {
                    ListadoNoticiasScreen(
                        navController = appNavController,
                        sessionManager = appContainer.sessionManager,
                        repository = appContainer.publicacionRepository,
                        imagenRepository = appContainer.imagenRepository,
                        reaccionRepository = appContainer.reaccionRepository,
                        favoritoRepository = appContainer.favoritoRepository,
                        onCommentClick = { contentId ->
                            contentNavController.navigate("comments_screen/$contentId")
                        },
                    )
                }

                composable(ScreenSections.Debates.route) {
                    ListadoDebatesScreen(
                        navController = appNavController,
                        sessionManager = appContainer.sessionManager,
                        repository = appContainer.publicacionRepository,
                        reaccionRepository = appContainer.reaccionRepository,
                        favoritoRepository = appContainer.favoritoRepository,
                        imagenRepository = appContainer.imagenRepository,
                        onCommentClick = { contentId ->
                            contentNavController.navigate("comments_screen/$contentId")
                        },
                    )
                }

                composable(ScreenSections.Favoritos.route) {
                    ListadoFavoritosScreen(
                        navController = appNavController,
                        sessionManager = appContainer.sessionManager,
                        repository = appContainer.publicacionRepository,
                        reaccionRepository = appContainer.reaccionRepository,
                        favoritoRepository = appContainer.favoritoRepository,
                        imagenRepository = appContainer.imagenRepository,
                        onCommentClick = { contentId ->
                            contentNavController.navigate("comments_screen/$contentId")
                        },
                    )
                }



                composable(
                    route = "comments_screen/{contentId}",
                    arguments = listOf(navArgument("contentId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val contentId = backStackEntry.arguments?.getLong("contentId") ?: 0L
                    ComentarioPublicacionScreen(
                        publicacionId = contentId,
                        sessionManager = appContainer.sessionManager,
                        comentarioRepository = appContainer.comentarioRepository,
                        publicacionRepository = appContainer.publicacionRepository,
                        imagenRepository = appContainer.imagenRepository,
                        onNavigateBack = { contentNavController.popBackStack() }
                    )
                }

                composable(Routes.CREATE_CONTENT_SCREEN) {
                    CrearPublicacionScreen(
                        sessionManager = appContainer.sessionManager,
                        repository = appContainer.publicacionRepository,
                        onNavigateBack = { appNavController.popBackStack() },
                        onNavigateToRoute = { route ->
                            contentNavController.navigate(route) {
                                popUpTo(contentNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }



                if (currentUserRole == UserRole.PROPIETARIO.name) {
                    composable(ScreenSections.Solicitudes.route) {
                        ListaSolicitudesScreen(
                            navController = appNavController,
                            sessionManager = appContainer.sessionManager,
                            solicitudModeradorRepository = appContainer.solicitudRepository
                        )
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        ConfirmDialog(
            title = "Cerrar sesión",
            message = "¿Estás seguro de que deseas cerrar sesión?",
            icon = Icons.Filled.ExitToApp,
            confirmText = "Cerrar sesión",
            cancelText = "Cancelar",
            isLoading = false,
            onConfirm = {
                authViewModel.logout()
                Result.Success(Unit)
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}
