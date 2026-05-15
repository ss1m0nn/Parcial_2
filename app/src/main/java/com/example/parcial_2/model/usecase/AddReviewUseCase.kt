package com.example.parcial_2.model.usecase

import com.example.parcial_2.model.data.Recipe
import com.example.parcial_2.model.data.ReviewRequest
import com.example.parcial_2.model.repository.RecipeRepository

class AddReviewUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(recipeId: String, user: String, comment: String, rating: Int): Result<Recipe> {
        return try {
            val reviewRequest = ReviewRequest(user, comment, rating)
            Result.success(repository.addReview(recipeId, reviewRequest))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
