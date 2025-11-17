package com.example.gaminghub.utils

private fun generateRandomPassword(length: Int = 8): String =
    (1..length).map { (('A'..'Z') + ('a'..'z') + ('0'..'9')).random() }.joinToString("")