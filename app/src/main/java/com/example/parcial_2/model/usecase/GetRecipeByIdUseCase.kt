package com.example.parcial_2.model.usecase

import com.example.parcial_2.model.data.Recipe
import com.example.parcial_2.model.repository.RecipeRepository

class GetRecipeByIdUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(id: String): Result<Recipe> {
        return try {
            Result.success(repository.getRecipeById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
