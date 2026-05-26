package com.example.parcial_2.model.data

data class OpinionRequest(
    val recetaId: String,
    val nombreComensal: String,
    val comentario: String,
    val calificacion: Double,
    val porciones: Int,
    val vecesPreparada: Int
)