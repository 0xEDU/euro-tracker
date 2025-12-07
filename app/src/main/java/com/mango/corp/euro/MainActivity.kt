package com.mango.corp.euro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { EuroTrackerScreen() }
    }
}

@Composable
fun EuroTrackerScreen() {
    var rate by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = withContext(Dispatchers.IO) {
                URL("https://api.frankfurter.app/latest?from=EUR&to=BRL").readText()
            }
            val json = JSONObject(response)
            val fetchedRate = json.getJSONObject("rates").getDouble("BRL")
            rate = "1 € = R$ %.2f".format(fetchedRate)
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        color = Color.Black
    ) {
        Box(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Color.White
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Option 1") },
                    onClick = { /* Do something... */ }
                )
                DropdownMenuItem(
                    text = { Text("Option 2") },
                    onClick = { /* Do something... */ }
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if (loading) {
                    Text("Loading...", color = Color.Gray)
                } else if (error != null) {
                    Text("Error: ${error}", color = Color.Red)
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        Text(
                            modifier = Modifier.padding(0.dp, 4.dp),
                            text = rate ?: "",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = "current euro price",
                            fontSize = 16.sp,
                            color = Color(0xFFFF5722),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Text(
                text = "Disclaimer: This information comes from a single source and may not be precise.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EuroTrackerScreen()
}