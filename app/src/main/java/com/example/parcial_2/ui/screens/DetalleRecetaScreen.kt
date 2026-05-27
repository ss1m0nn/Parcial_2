package com.example.parcial_2.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.parcial_2.model.data.Receta
import com.example.parcial_2.viewmodel.EstadisticasViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.parcial_2.viewmodel.EstadisticasUiState

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRecetaScreen(
    receta: Receta,
    viewModel: EstadisticasViewModel,
    onBack: () -> Unit,
    onOpinionClick: () -> Unit
) {
    LaunchedEffect(receta.id) {
        viewModel.cargarDatos(receta.id)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(receta.nombre) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen de la receta
            AsyncImage(
                model = receta.imagen,
                contentDescription = receta.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // Fila de Info Rápida
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SuggestionChip(
                        onClick = { },
                        label = { Text(receta.categoria) },
                        shape = RoundedCornerShape(16.dp)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", receta.puntuacionPromedio),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    InfoItem(label = "Tiempo", value = "${receta.tiempoPreparacion} min")
                    InfoItem(label = "Dificultad", value = calcularDificultad(receta.tiempoPreparacion))
                    InfoItem(label = "Rinde", value = "${receta.porciones} pers.")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onOpinionClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text("Registrar puntuación de la receta", fontWeight = FontWeight.Bold)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                // Ingredientes
                Text(text = "Ingredientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                receta.ingredientes.forEach { ingrediente ->
                    Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(MaterialTheme.colorScheme.secondary, CircleShape))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = ingrediente, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Preparación
                Text(text = "Preparación", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                receta.pasos.forEachIndexed { index, paso ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Text(text = "${index + 1}", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = paso, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                // Estadísticas
                Text("Estadísticas Detalladas", style = MaterialTheme.typography.titleMedium)

                when (val state = uiState) {
                    is EstadisticasUiState.Loading -> Text("Cargando...")
                    is EstadisticasUiState.Success -> {
                        Text("Veces preparada: ${state.data.totalPreparaciones}")
                        Text("Opiniones totales: ${state.data.totalOpiniones}")
                    }
                    is EstadisticasUiState.Error -> Text("Error cargando estadísticas")
                    else -> {}
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}

fun calcularDificultad(tiempo: Int): String {
    return when {
        tiempo <= 20 -> "Fácil"
        tiempo <= 45 -> "Media"
        else -> "Difícil"
    }
}


