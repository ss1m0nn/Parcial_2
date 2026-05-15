package com.example.parcial_2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parcial_2.model.repository.RecipeRepository
import com.example.parcial_2.model.usecase.AddReviewUseCase
import com.example.parcial_2.model.usecase.GetRecipeByIdUseCase
import com.example.parcial_2.model.usecase.GetRecipesUseCase

class RecipeViewModelFactory(private val repository: RecipeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            val getRecipesUseCase = GetRecipesUseCase(repository)
            val getRecipeByIdUseCase = GetRecipeByIdUseCase(repository)
            val addReviewUseCase = AddReviewUseCase(repository)
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(getRecipesUseCase, getRecipeByIdUseCase, addReviewUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
