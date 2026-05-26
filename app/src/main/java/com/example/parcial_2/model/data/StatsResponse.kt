package com.example.parcial_2.model.data

data class StatsResponse(
    val puntuacionPromedio: Double,
    val totalPreparaciones: Int,
    val totalOpiniones: Int,
    val tiempoPreparacion: String,
    val porciones: Int
)