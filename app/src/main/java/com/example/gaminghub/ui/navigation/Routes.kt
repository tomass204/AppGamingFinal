package com.example.gaminghub.ui.navigation

object Routes {
    const val AUTH_SCREEN = "auth_screen"
    const val MAIN_SCREEN = "main_screen"
    const val REGISTRATION_SUCCESS_SCREEN = "registration_success_screen"
    const val RECOVERY_SUCCESS_SCREEN = "recovery_success_screen"
    const val CREATE_CONTENT_SCREEN = "create_content_screen"
    const val COMMENTS_SCREEN = "comments_screen/{contentId}"
    const val REQUESTS_SCREEN = "requests_screen"

    const val USER_UPDATED_SCREEN = "user_updated_screen"
    fun createCommentsRoute(contentId: Long) = "comments_screen/$contentId"
}