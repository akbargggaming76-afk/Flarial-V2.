
package com.flarial.v2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
                onPlay = { launchMinecraft() }
            )
        }
    }

    private fun launchMinecraft() {
        val intent = packageManager.getLaunchIntentForPackage(
            "com.mojang.minecraftpe"
        )

        if (intent != null) {
            startActivity(intent)
        } else {
            Toast.makeText(
                this,
                "Minecraft tidak ditemukan!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

private val Purple = Color(0xFF9B5CFF)
private val Dark = Color(0xFF100B1D)
private val CardColor = Color(0xFF211832)

@Composable
fun FlarialApp(onPlay: () -> Unit) {

    var page by remember { mutableStateOf("home") }

    val context = androidx.compose.ui.platform.LocalContext.current

    var importType by remember { mutableStateOf("addon") }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            val mimeIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "*/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            try {
                context.startActivity(mimeIntent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Tidak dapat membuka file ini",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF281442),
                        Dark,
                        Color(0xFF080610)
                    )
                )
            )
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
                        fontSize = 34.sp
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Minecraft Bedrock Companion",
                        color = Purple
                    )

                    Spacer(Modifier.height(55.dp))

                    Button(
                        onClick = onPlay,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Purple
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(
                            "PLAY",
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Minecraft Bedrock",
                        color = Color.LightGray
                    )

                    Spacer(Modifier.height(65.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { page = "settings" },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = CardColor
                            )
                        ) {
                            Text("Setting", color = Color.White)
                        }

                        Button(
                            onClick = { page = "about" },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = CardColor
                            )
                        ) {
                            Text("Tentang", color = Color.White)
                        }
                    }
                }
            }

            "settings" -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    Text(
                        "SETTING",
                        color = Color.White,
                        fontSize = 28.sp
                    )

                    Spacer(Modifier.height(25.dp))

                    ImportButton("Addons") {
                        importType = "addon"
                        filePicker.launch(
                            arrayOf("*/*")
                        )
                    }

                    ImportButton("Resource Packs") {
                        importType = "resource"
                        filePicker.launch(
                            arrayOf("*/*")
                        )
                    }

                    ImportButton("Worlds") {
                        importType = "world"
                        filePicker.launch(
                            arrayOf("*/*")
                        )
                    }

                    Spacer(Modifier.height(25.dp))

                    Button(
                        onClick = { page = "home" }
                    ) {
                        Text("Kembali")
                    }
                }
            }

            "about" -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        "TENTANG FLARIAL V2",
                        color = Color.White,
                        fontSize = 25.sp
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "FLARIAL V2 v0.1",
                        color = Purple,
                        fontSize = 19.sp
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Minecraft Bedrock Companion App",
                        color = Color.LightGray
                    )

                    Spacer(Modifier.height(35.dp))

                    Button(
                        onClick = { page = "home" }
                    ) {
                        Text("Kembali")
                    }
                }
            }
        }
    }
}

@Composable
fun ImportButton(
    title: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
            .height(55.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = CardColor
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(
            title,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}
