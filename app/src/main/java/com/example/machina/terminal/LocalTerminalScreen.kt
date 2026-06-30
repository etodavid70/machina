package com.example.machina.terminal

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.ui.theme.OnlineGreen
import com.example.machina.ui.theme.terminalChrome
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.BackButton

/**
 * LocalTerminalScreen - Pure Kotlin/Java terminal emulator (no NDK required)
 * Uses ProcessBuilder to execute shell commands
 */
@Composable
fun LocalTerminalScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    
    val terminalOutput = remember { mutableStateOf("") }
    val commandInput = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val shellSession = remember { mutableStateOf<LocalShellSession?>(null) }

    fun leaveTerminal() {
        shellSession.value?.terminate()
        navController.popBackStack()
    }

    BackHandler(onBack = ::leaveTerminal)

    DisposableEffect(Unit) {
        val session = LocalShellSession(
            context = context,
            onOutput = { output ->
                terminalOutput.value += output
            },
            onError = { error ->
                terminalOutput.value += "\u001B[31m$error\u001B[0m"
            },
            onClose = {
                terminalOutput.value += "\n--- Shell closed ---\n"
            }
        )
        session.start()
        shellSession.value = session

        onDispose {
            session.terminate()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(padding)
        ) {
            // Top header bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(terminalChrome)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(onClick = ::leaveTerminal, modifier = Modifier)
                Column(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .weight(1f)
                ) {
                    AppText(
                        text = "Display OS",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    AppText(
                        text = "Local shell",
                        fontSize = 12.sp,
                        color = Color(0xFF8EA39B),
                        maxLines = 1
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(OnlineGreen)
                    )
                    AppText(
                        text = "Live",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnlineGreen,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    IconButton(
                        onClick = {
                            keyboardController?.show()
                        },
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Keyboard,
                            contentDescription = "Show keyboard",
                            tint = Color.White
                        )
                    }
                }
            }

            // Terminal output area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    AppText(
                        text = terminalOutput.value,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF00FF00),
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            // Input field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1a1a1a))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = commandInput.value,
                    onValueChange = { commandInput.value = it },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF2a2a2a)),
                    textStyle = androidx.compose.material3.LocalTextStyle.current.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = Color(0xFF00FF00)
                    ),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFF2a2a2a),
                        focusedContainerColor = Color(0xFF2a2a2a),
                        focusedTextColor = Color(0xFF00FF00),
                        unfocusedTextColor = Color(0xFF00FF00),
                        cursorColor = Color(0xFF00FF00),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
                
                IconButton(
                    onClick = {
                        if (commandInput.value.isNotEmpty()) {
                            shellSession.value?.write(commandInput.value + "\n")
                            commandInput.value = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    AppText(
                        text = "↵",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00FF00)
                    )
                }
            }
        }
    }
}
