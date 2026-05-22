package com.example.parcial_2.model.data
import kotlin.collections.List

data class Receta(
    val id: String,
    val nombre: String,
    val imagen: String,
    val ingredientes: List<String>,
    val pasos: List<String>,
    val categoria: String,
    val tiempoPreparacion: Int,
    val vecesPreparada: Int,
    val puntuacionPromedio: Double
)