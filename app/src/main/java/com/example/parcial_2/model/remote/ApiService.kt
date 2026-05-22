package com.example.parcial_2.model.remote

import com.example.parcial_2.model.data.Receta
import com.example.parcial_2.model.data.RecetaRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("api/recetas")
    suspend fun getRecetas(): ApiResponse<List<Receta>>

    @POST("api/recetas")
    suspend fun crearReceta(@Body receta: RecetaRequest): ApiResponse<Receta>

    @PUT("api/recetas/{id}")
    suspend fun actualizarReceta(@Path("id") id: String, @Body receta: RecetaRequest): ApiResponse<Receta>

    @DELETE("api/recetas/{id}")
    suspend fun eliminarReceta(@Path("id") id: String): ApiResponse<Unit>
}