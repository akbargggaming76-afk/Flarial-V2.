
package com.flarial.v2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

    private val overlayPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (Settings.canDrawOverlays(this)) {
                launchMinecraft()
            } else {
                Toast.makeText(
                    this,
                    "Izin Floating Overlay belum diberikan",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FlarialApp(
                onPlay = { requestOverlayAndPlay() }
            )
        }
    }

    private fun requestOverlayAndPlay() {
        if (Settings.canDrawOverlays(this)) {
            launchMinecraft()
        } else {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            overlayPermissionLauncher.launch(intent)
        }
    }

    private fun launchMinecraft() {
        val packageName = "com.mojang.minecraftpe"

        val minecraftIntent =
            packageManager.getLaunchIntentForPackage(packageName)

        if (minecraftIntent == null) {
            Toast.makeText(
                this,
                "Minecraft tidak ditemukan!",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        try {
            startService(
                Intent(this, FloatingService::class.java)
            )

            startActivity(minecraftIntent)

        } catch (error: Exception) {
            Toast.makeText(
                this,
                "Gagal membuka Minecraft: ${error.message}",
                Toast.LENGTH_LONG
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
            secondary = Color(0xFF9B55E8)
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
                            horizontalArrangement =
                                Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { page = "settings" }
                            ) {
                                Text("Setting")
                            }

                            Button(
                                onClick = { page = "about" }
                            ) {
                                Text("Tentang")
                            }
                        }
                    }
                }

                "settings" -> {
                    SettingsPage {
                        page = "home"
                    }
                }

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

    var selectedFile by remember {
        mutableStateOf("Belum ada file dipilih")
    }

    val filePicker =
        androidx.activity.compose.rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->
            if (uri != null) {
                selectedFile =
                    uri.lastPathSegment ?: "File dipilih"
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
                filePicker.launch(arrayOf("*/*"))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Import Addons")
        }

        Button(
            onClick = {
                filePicker.launch(arrayOf("*/*"))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Resource Packs")
        }

        Button(
            onClick = {
                filePicker.launch(arrayOf("*/*"))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Worlds")
        }

        Spacer(Modifier.height(20.dp))

        Text(
            selectedFile,
            color = Color.LightGray
        )

        Spacer(Modifier.height(30.dp))

        Button(onClick = onBack) {
            Text("Kembali")
        }
    }
}
