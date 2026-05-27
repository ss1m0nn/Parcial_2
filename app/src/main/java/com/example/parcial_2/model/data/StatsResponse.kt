package com.example.parcial_2.model.data

data class StatsResponse(
    val totalPreparaciones: Int,
    val totalOpiniones: Int,
    val notas: List<String>
)