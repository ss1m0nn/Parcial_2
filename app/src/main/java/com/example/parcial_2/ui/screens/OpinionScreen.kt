package com.example.parcial_2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.parcial_2.model.data.Receta
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpinionScreen(
    receta: Receta,
    onBack: () -> Unit,
    onSubmit: (Int, Double, String, Int) -> Unit
) {
    var porciones by remember { mutableStateOf(1) }
    var puntuacion by remember { mutableFloatStateOf(0.0f) }
    var notaPersonal by remember { mutableStateOf("") }
    var vecesHecha by remember { mutableIntStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Valorar: ${receta.nombre}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Sección de Porciones
            Column {
                Text("¿Para cuántas personas rindió?", fontWeight = FontWeight.Bold)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    IconButton(onClick = { if (porciones > 1) porciones-- }) {
                        Icon(Icons.Default.Remove, contentDescription = null)
                    }
                    Text(
                        text = porciones.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    IconButton(onClick = { porciones++ }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                    Text("personas", color = Color.Gray)
                }
            }

            // Sección de Puntuación (Slider para decimales)
            Column {
                Text("Puntuación del plato", fontWeight = FontWeight.Bold)
                Text(
                    text = String.format("%.1f estrellas", puntuacion),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                Slider(
                    value = puntuacion,
                    onValueChange = { puntuacion = (it * 10).roundToInt() / 10f },
                    valueRange = 0f..5f,
                    steps = 49, // 0.1 increments
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(5) { index ->
                        val starRating = index + 1
                        val icon = when {
                            puntuacion >= starRating -> Icons.Default.Star
                            puntuacion > index -> Icons.Default.Star // Simplified for visual feedback
                            else -> Icons.Outlined.Star
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (puntuacion > index) Color(0xFFFFC107) else Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            // Nota Personal
            OutlinedTextField(
                value = notaPersonal,
                onValueChange = { if (it.length <= 100) notaPersonal = it },
                label = { Text("Nota personal corta") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ej: Me quedó un poco salado...") },
                supportingText = { Text("${notaPersonal.length}/100") }
            )

            // Contador de Veces Hecha
            Column {
                Text("¿Cuántas veces has hecho esta receta?", fontWeight = FontWeight.Bold)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    OutlinedButton(onClick = { if (vecesHecha > 0) vecesHecha-- }) {
                        Icon(Icons.Default.Remove, contentDescription = null)
                    }
                    Text(
                        text = vecesHecha.toString(),
                        modifier = Modifier.padding(horizontal = 24.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    OutlinedButton(onClick = { vecesHecha++ }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
            }

            Button(
                onClick = { onSubmit(porciones, puntuacion.toDouble(), notaPersonal, vecesHecha) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = puntuacion > 0
            ) {
                Text("Guardar Opinión")
            }
        }
    }
}
