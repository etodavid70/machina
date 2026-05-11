package com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.BackButton
import com.example.machina.view_model.dashboard_viewmodel.SshConnectionViewModel
import com.example.machina.view_model.dashboard_viewmodel.TerminalCommandUiState
import com.example.machina.view_model.dashboard_viewmodel.TerminalLineType

@Composable
fun TerminalScreen(
    navController: NavController,
    viewModel: SshConnectionViewModel
) {
    val terminalLines by viewModel.terminalLines.collectAsState()
    val terminalState by viewModel.terminalState.collectAsState()
    val listState = rememberLazyListState()
    var command by remember { mutableStateOf("") }
    val isRunning = terminalState is TerminalCommandUiState.Running
    val hasConnection = viewModel.hasActiveConnection()

    LaunchedEffect(terminalLines.size) {
        if (terminalLines.isNotEmpty()) {
            listState.animateScrollToItem(terminalLines.lastIndex)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(
                    navController = navController,
                    modifier = Modifier
                )

                AppText(
                    "Terminal",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = viewModel::clearTerminal,
                    enabled = terminalLines.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear terminal"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (!hasConnection) {
                Text(
                    text = "Connect to a server first to use the terminal.",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF101418),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(12.dp)
            ) {
                if (terminalLines.isEmpty()) {
                    item {
                        Text(
                            text = "Connected. Type a command below.",
                            color = Color(0xFF9CA3AF),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        )
                    }
                }

                items(terminalLines) { line ->
                    Text(
                        text = line.text,
                        color = when (line.type) {
                            TerminalLineType.Prompt -> AppGreen
                            TerminalLineType.Output -> Color(0xFFE5E7EB)
                            TerminalLineType.Error -> Color(0xFFFF8A80)
                        },
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = command,
                    onValueChange = { command = it },
                    placeholder = { Text("Enter command") },
                    singleLine = true,
                    enabled = hasConnection && !isRunning,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppGreen,
                        unfocusedBorderColor = AppGreen
                    ),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        viewModel.runTerminalCommand(command)
                        command = ""
                    },
                    enabled = hasConnection && !isRunning && command.isNotBlank()
                ) {
                    if (isRunning) {
                        CircularProgressIndicator(
                            modifier = Modifier.height(22.dp),
                            strokeWidth = 2.dp,
                            color = AppGreen
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Run command",
                            tint = AppGreen
                        )
                    }
                }
            }
        }
    }
}
