package com.example.parcial_2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.parcial_2.model.data.Recipe
import com.example.parcial_2.viewmodel.RecipeViewModel
import com.example.parcial_2.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: String,
    viewModel: RecipeViewModel,
    onBackClick: () -> Unit
) {
    val detailState by viewModel.selectedRecipeState.collectAsState()

    LaunchedEffect(recipeId) {
        viewModel.fetchRecipeById(recipeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Receta") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = detailState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Success -> {
                    RecipeDetailContent(recipe = state.data, viewModel = viewModel)
                }
                is UiState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeDetailContent(recipe: Recipe, viewModel: RecipeViewModel) {
    var showReviewDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = recipe.name, style = MaterialTheme.typography.headlineMedium)
            Text(text = recipe.category, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = recipe.description, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                InfoColumn(label = "Tiempo", value = "${recipe.preparationTime} min")
                InfoColumn(label = "Puntuación", value = "⭐ ${recipe.averageScore}")
                InfoColumn(label = "Preparada", value = "${recipe.timesPrepared} veces")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Ingredientes", style = MaterialTheme.typography.titleLarge)
        }
        
        items(recipe.ingredients) { ingredient ->
            Text(text = "• $ingredient", modifier = Modifier.padding(vertical = 4.dp))
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Pasos", style = MaterialTheme.typography.titleLarge)
        }

        items(recipe.steps.withIndex().toList()) { (index, step) ->
            Text(text = "${index + 1}. $step", modifier = Modifier.padding(vertical = 8.dp))
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Opiniones", style = MaterialTheme.typography.titleLarge)
                Button(onClick = { showReviewDialog = true }) {
                    Text("Opinar")
                }
            }
        }

        items(recipe.reviews) { review ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = review.user, style = MaterialTheme.typography.titleSmall)
                        Text(text = "⭐ ${review.rating}")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = review.comment, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }

    if (showReviewDialog) {
        AddReviewDialog(
            onDismiss = { showReviewDialog = false },
            onConfirm = { user, comment, rating ->
                viewModel.addReview(recipe.id, user, comment, rating)
                showReviewDialog = false
            }
        )
    }
}

@Composable
fun InfoColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun AddReviewDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int) -> Unit
) {
    var user by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(5) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir Opinión") },
        text = {
            Column {
                TextField(value = user, onValueChange = { user = it }, label = { Text("Nombre") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = comment, onValueChange = { comment = it }, label = { Text("Comentario") })
                Spacer(modifier = Modifier.height(8.dp))
                Text("Puntuación: $rating")
                Slider(
                    value = rating.toFloat(),
                    onValueChange = { rating = it.toInt() },
                    valueRange = 1f..5f,
                    steps = 3
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(user, comment, rating) }) {
                Text("Enviar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
