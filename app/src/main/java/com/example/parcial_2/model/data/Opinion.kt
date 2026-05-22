package com.example.parcial_2.model.data

data class Opinion(
    val id: String,
    val recetaId: String,
    val nombreComensal: String,
    val comentario: String,
    val calificacion: Int,
    val fecha: String
)