package com.example.parcial_2.model.repository

import com.example.parcial_2.model.data.Recipe
import com.example.parcial_2.model.data.ReviewRequest
import com.example.parcial_2.model.remote.RecipeApi

class RecipeRepository(private val api: RecipeApi) {
    suspend fun getRecipes(): List<Recipe> = api.getRecipes()
    suspend fun getRecipeById(id: String): Recipe = api.getRecipeById(id)
    suspend fun addReview(id: String, review: ReviewRequest): Recipe = api.addReview(id, review)
}
