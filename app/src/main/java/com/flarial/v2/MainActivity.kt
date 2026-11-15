
package com.flarial.v2

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FlarialApp(
                onPlay = { openMinecraft() }
            )
        }
    }

    private fun openMinecraft() {
        val packageName = "com.mojang.minecraftpe"

        val launchIntent =
            packageManager.getLaunchIntentForPackage(packageName)

        if (launchIntent != null) {
            startActivity(launchIntent)
        } else {
            android.widget.Toast.makeText(
                this,
                "Minecraft tidak ditemukan!",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
fun FlarialApp(onPlay: () -> Unit) {

    var page by remember { mutableStateOf("home") }

    val background = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF251044),
            Color(0xFF10091E),
            Color(0xFF080610)
        )
    )

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFB56CFF),
            secondary = Color(0xFF9B55E8),
            background = Color(0xFF10091E)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background)
                .padding(22.dp)
        ) {
            when (page) {
                "home" -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "FLARIAL V2",
                            color = Color.White,
                            fontSize = 30.sp
                        )

                        Spacer(Modifier.height(50.dp))

                        Button(
                            onClick = onPlay,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(65.dp),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Text("PLAY", fontSize = 22.sp)
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            "Minecraft Bedrock",
                            color = Color.LightGray
                        )

                        Spacer(Modifier.height(60.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(onClick = { page = "settings" }) {
                                Text("Setting")
                            }

                            Button(onClick = { page = "about" }) {
                                Text("Tentang")
                            }
                        }
                    }
                }

                "settings" -> SettingsPage(
                    onBack = { page = "home" }
                )

                "about" -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Tentang FLARIAL V2",
                            color = Color.White,
                            fontSize = 24.sp
                        )

                        Spacer(Modifier.height(15.dp))

                        Text(
                            "Minecraft Bedrock Companion",
                            color = Color.LightGray
                        )

                        Spacer(Modifier.height(30.dp))

                        Button(onClick = { page = "home" }) {
                            Text("Kembali")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsPage(onBack: () -> Unit) {

    var selectedFiles by remember {
        mutableStateOf("Belum ada file dipilih")
    }

    val filePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            if (uri != null) {
                selectedFiles = uri.lastPathSegment ?: "File dipilih"
            }
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "SETTING",
            color = Color.White,
            fontSize = 27.sp
        )

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = {
                filePicker.launch(
                    arrayOf(
                        "application/octet-stream",
                        "application/zip",
                        "*/*"
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Import Addons")
        }

        Button(
            onClick = {
                filePicker.launch(
                    arrayOf("*/*")
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Resource Packs")
        }

        Button(
            onClick = {
                filePicker.launch(
                    arrayOf("*/*")
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Worlds")
        }

        Spacer(Modifier.height(20.dp))

        Text(
            selectedFiles,
            color = Color.LightGray
        )

        Spacer(Modifier.height(30.dp))

        Button(onClick = onBack) {
            Text("Kembali")
        }
    }
}
