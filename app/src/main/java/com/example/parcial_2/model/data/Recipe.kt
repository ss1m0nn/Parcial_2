package com.example.parcial_2.model.data

data class Recipe(
    val id: String,
    val name: String,
    val description: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val category: String,
    val preparationTime: Int, // in minutes
    val averageScore: Double,
    val timesPrepared: Int,
    val reviews: List<Review>,
    val imageUrl: String
)
