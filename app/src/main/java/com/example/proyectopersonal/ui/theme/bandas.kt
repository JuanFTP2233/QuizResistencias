package com.example.proyectopersonal.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF5E35B1),
                    secondary = Color(0xFF7C4DFF),
                    surface = Color(0xFFF3F1F8),
                    onPrimary = Color.White,
                    onSecondary = Color.White
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ResistorCalculator()
                }
            }
        }
    }
}

@Composable
fun ResistorCalculator() {
    val colors = listOf("Negro", "Marrón", "Rojo", "Naranja", "Amarillo", "Verde", "Azul", "Violeta", "Gris", "Blanco")
    val multipliers = listOf("Negro", "Marrón", "Rojo", "Naranja", "Amarillo")
    val tolerances = listOf("Dorado", "Plateado", "Ninguno")

    val colorMap = mapOf(
        "Negro" to Pair(0, Color.Black),
        "Marrón" to Pair(1, Color(0xFFA52A2A)),
        "Rojo" to Pair(2, Color.Red),
        "Naranja" to Pair(3, Color(0xFFFFA500)),
        "Amarillo" to Pair(4, Color.Yellow),
        "Verde" to Pair(5, Color(0xFF4CAF50)),
        "Azul" to Pair(6, Color(0xFF2196F3)),
        "Violeta" to Pair(7, Color(0xFF8E24AA)),
        "Gris" to Pair(8, Color.Gray),
        "Blanco" to Pair(9, Color.White),
        "Dorado" to Pair(-1, Color(0xFFFFD700)),
        "Plateado" to Pair(-2, Color.LightGray),
        "Ninguno" to Pair(-3, Color(0xFFE0E0E0))
    )

    var band1 by remember { mutableStateOf("Negro") }
    var band2 by remember { mutableStateOf("Negro") }
    var band3 by remember { mutableStateOf("Negro") }
    var band4 by remember { mutableStateOf("Dorado") }

    val value = (colorMap[band1]?.first ?: 0) * 10 + (colorMap[band2]?.first ?: 0)
    val multiplier = 10.0.pow((colorMap[band3]?.first ?: 0).toDouble())
    val resistance = value * multiplier
    val tolerance = when (band4) {
        "Dorado" -> "±5%"
        "Plateado" -> "±10%"
        else -> "±20%"
    }

    val backgroundColor = colorMap[band1]?.second ?: Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor.copy(alpha = 0.05f))
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Calculadora de Resistencias",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            BandDropdown("Banda 1", colors, band1, { band1 = it }, colorMap)
            BandDropdown("Banda 2", colors, band2, { band2 = it }, colorMap)
            BandDropdown("Multiplicador", multipliers, band3, { band3 = it }, colorMap)
            BandDropdown("Tolerancia", tolerances, band4, { band4 = it }, colorMap)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(min = 200.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Resultado",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "${resistance.toInt()} Ω $tolerance",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
fun BandDropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    colorMap: Map<String, Pair<Int, Color>>
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = colorMap[selected]?.second?.copy(alpha = 0.15f) ?: Color.Transparent,
                    contentColor = Color.Black
                )
            ) {
                Text(selected, fontSize = 16.sp)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
