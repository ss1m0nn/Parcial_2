package com.example.parcial_2.model.usecase

import com.example.parcial_2.model.data.Recipe
import com.example.parcial_2.model.repository.RecipeRepository

class GetRecipesUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(): Result<List<Recipe>> {
        return try {
            Result.success(repository.getRecipes())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
