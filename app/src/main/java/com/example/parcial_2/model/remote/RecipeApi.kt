package com.example.parcial_2.model.remote

import com.example.parcial_2.model.data.Recipe
import com.example.parcial_2.model.data.ReviewRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RecipeApi {
    @GET("recipes")
    suspend fun getRecipes(): List<Recipe>

    @GET("recipes/{id}")
    suspend fun getRecipeById(@Path("id") id: String): Recipe

    @POST("recipes/{id}/reviews")
    suspend fun addReview(
        @Path("id") id: String,
        @Body review: ReviewRequest
    ): Recipe
}
