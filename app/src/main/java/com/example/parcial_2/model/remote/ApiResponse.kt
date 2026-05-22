package com.example.parcial_2.model.remote

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null
)