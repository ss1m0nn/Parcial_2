package com.example.parcial_2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial_2.model.data.Recipe
import com.example.parcial_2.model.usecase.AddReviewUseCase
import com.example.parcial_2.model.usecase.GetRecipeByIdUseCase
import com.example.parcial_2.model.usecase.GetRecipesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class RecipeViewModel(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val addReviewUseCase: AddReviewUseCase
) : ViewModel() {

    private val _recipesState = MutableStateFlow<UiState<List<Recipe>>>(UiState.Loading)
    val recipesState: StateFlow<UiState<List<Recipe>>> = _recipesState.asStateFlow()

    private val _selectedRecipeState = MutableStateFlow<UiState<Recipe>>(UiState.Loading)
    val selectedRecipeState: StateFlow<UiState<Recipe>> = _selectedRecipeState.asStateFlow()

    init {
        fetchRecipes()
    }

    fun fetchRecipes() {
        viewModelScope.launch {
            _recipesState.value = UiState.Loading
            getRecipesUseCase().fold(
                onSuccess = { _recipesState.value = UiState.Success(it) },
                onFailure = { _recipesState.value = UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun fetchRecipeById(id: String) {
        viewModelScope.launch {
            _selectedRecipeState.value = UiState.Loading
            getRecipeByIdUseCase(id).fold(
                onSuccess = { _selectedRecipeState.value = UiState.Success(it) },
                onFailure = { _selectedRecipeState.value = UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun addReview(recipeId: String, user: String, comment: String, rating: Int) {
        viewModelScope.launch {
            addReviewUseCase(recipeId, user, comment, rating).fold(
                onSuccess = { 
                    _selectedRecipeState.value = UiState.Success(it)
                    // Optionally refresh the list if needed, though usually detail view is enough
                },
                onFailure = { /* Handle error */ }
            )
        }
    }
}
