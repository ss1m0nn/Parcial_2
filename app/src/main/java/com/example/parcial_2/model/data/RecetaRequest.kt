package com.example.parcial_2.model.data

data class RecetaRequest(
    val nombre: String,
    val imagen: String,
    val ingredientes: List<String>,
    val pasos: List<String>,
    val categoria: String,
    val tiempoPreparacion: Int
)