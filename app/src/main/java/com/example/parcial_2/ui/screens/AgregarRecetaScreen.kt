package com.example.parcial_2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parcial_2.model.data.Receta
import com.example.parcial_2.viewmodel.AgregarRecetaViewModel
import com.example.parcial_2.viewmodel.AgregarUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarRecetaScreen(
    onBack: () -> Unit,
    recetaExistente: Receta? = null,
    viewModel: AgregarRecetaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var nombre by remember { mutableStateOf(recetaExistente?.nombre ?: "") }
    var imagen by remember { mutableStateOf(recetaExistente?.imagen ?: "") }
    var categoria by remember { mutableStateOf(recetaExistente?.categoria ?: "") }
    var tiempoPreparacion by remember { mutableStateOf(recetaExistente?.tiempoPreparacion?.toString() ?: "") }
    var porciones by remember { mutableStateOf(recetaExistente?.porciones?.toString() ?: "") }

    val ingredientes = remember { mutableStateListOf<String>().also { it.addAll(recetaExistente?.ingredientes ?: emptyList()) } }
    val pasos = remember { mutableStateListOf<String>().also { it.addAll(recetaExistente?.pasos ?: emptyList()) } }

    var nuevoIngrediente by remember { mutableStateOf("") }
    var nuevoPaso by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is AgregarUiState.Success) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (recetaExistente == null) "Nueva Receta" else "Editar Receta") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = imagen,
                onValueChange = { imagen = it },
                label = { Text("URL de imagen") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = tiempoPreparacion,
                onValueChange = { tiempoPreparacion = it },
                label = { Text("Tiempo de preparación (min)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = porciones,
                onValueChange = { porciones = it },
                label = { Text("Porciones") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // ingredientes section
            Text("Ingredientes", fontWeight = FontWeight.SemiBold)

            ingredientes.forEachIndexed { index, ingrediente ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "• $ingrediente", modifier = Modifier.weight(1f))
                    IconButton(onClick = { ingredientes.removeAt(index) }) {
                        Icon(Icons.Default.Close, contentDescription = "eliminar")
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = nuevoIngrediente,
                    onValueChange = { nuevoIngrediente = it },
                    label = { Text("Agregar ingrediente") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (nuevoIngrediente.isNotBlank()) {
                            ingredientes.add(nuevoIngrediente.trim())
                            nuevoIngrediente = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "agregar")
                }
            }

            // pasos section
            Text("Pasos", fontWeight = FontWeight.SemiBold)

            pasos.forEachIndexed { index, paso ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "${index + 1}. $paso", modifier = Modifier.weight(1f))
                    IconButton(onClick = { pasos.removeAt(index) }) {
                        Icon(Icons.Default.Close, contentDescription = "eliminar")
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = nuevoPaso,
                    onValueChange = { nuevoPaso = it },
                    label = { Text("Agregar paso") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (nuevoPaso.isNotBlank()) {
                            pasos.add(nuevoPaso.trim())
                            nuevoPaso = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "agregar")
                }
            }

            if (uiState is AgregarUiState.Error) {
                Text(
                    text = (uiState as AgregarUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    viewModel.guardarReceta(
                        id = recetaExistente?.id,
                        nombre = nombre,
                        imagen = imagen,
                        ingredientes = ingredientes.toList(),
                        pasos = pasos.toList(),
                        categoria = categoria,
                        tiempoPreparacion = tiempoPreparacion.toIntOrNull() ?: 0,
                        porciones = porciones.toIntOrNull() ?: 1
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is AgregarUiState.Loading
            ) {
                if (uiState is AgregarUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text(if (recetaExistente == null) "Guardar Receta" else "Actualizar Receta")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}