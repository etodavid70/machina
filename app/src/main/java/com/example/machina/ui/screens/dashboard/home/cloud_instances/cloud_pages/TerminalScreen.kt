package com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_pages

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.getSystemService
import androidx.navigation.NavController
import com.example.machina.data.repository.SshShellConnection
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.BackButton
import com.example.machina.view_model.dashboard_viewmodel.SshConnectionViewModel
import jackpal.androidterm.emulatorview.ColorScheme
import jackpal.androidterm.emulatorview.EmulatorView
import jackpal.androidterm.emulatorview.TermSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun TerminalScreen(
    navController: NavController,
    viewModel: SshConnectionViewModel
) {
    var terminalSession by remember { mutableStateOf<TermSession?>(null) }
    var shellConnection by remember { mutableStateOf<SshShellConnection?>(null) }
    var terminalView by remember { mutableStateOf<EmulatorView?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val currentTerminalSession by rememberUpdatedState(terminalSession)
    val currentShellConnection by rememberUpdatedState(shellConnection)
    val currentTerminalView by rememberUpdatedState(terminalView)

    LaunchedEffect(Unit) {
        errorMessage = null

        try {
            val shell = withContext(Dispatchers.IO) {
                viewModel.openInteractiveShell()
            }
            val session = TermSession().apply {
                setTermIn(shell.input)
                setTermOut(shell.output)
                setDefaultUTF8Mode(true)
                setColorScheme(ColorScheme(0xFFE5E7EB.toInt(), 0xFF101418.toInt()))
                initializeEmulator(DEFAULT_COLUMNS, DEFAULT_ROWS)
            }

            shellConnection = shell
            terminalSession = session
        } catch (e: Exception) {
            errorMessage = e.message?.takeIf { it.isNotBlank() }
                ?: "Could not open interactive terminal."
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            currentTerminalView?.onPause()
            currentTerminalSession?.finish()
            currentShellConnection?.disconnect()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF101418))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
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
            }

            when {
                errorMessage != null -> {
                    Text(
                        text = errorMessage.orEmpty(),
                        color = Color(0xFFFF8A80),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                terminalSession == null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF101418)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(32.dp))
                        CircularProgressIndicator(color = Color(0xFF4DC591))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Opening interactive shell...",
                            color = Color(0xFFE5E7EB)
                        )
                    }
                }

                else -> {
                    AndroidView(
                        factory = { viewContext ->
                            EmulatorView(
                                viewContext,
                                terminalSession,
                                viewContext.resources.displayMetrics
                            ).apply {
                                terminalView = this
                                setTextSize(14)
                                setTermType("xterm-256color")
                                setUseCookedIME(false)
                                setFocusableInTouchMode(true)
                                requestFocus()
                                onResume()

                                addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ ->
                                    resizeRemotePty(view, shellConnection)
                                }

                                post {
                                    updateSize(true)
                                    resizeRemotePty(this, shellConnection)
                                    requestFocus()
                                    viewContext
                                        .getSystemService<InputMethodManager>()
                                        ?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                                }
                            }
                        },
                        update = { view ->
                            view.onResume()
                            view.requestFocus()
                            view.updateSize(true)
                            resizeRemotePty(view, shellConnection)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF101418))
                    )
                }
            }
        }
    }
}

private fun resizeRemotePty(
    view: View,
    shellConnection: SshShellConnection?
) {
    val emulatorView = view as? EmulatorView ?: return
    val columns = emulatorView.visibleColumns
    val rows = emulatorView.visibleRows

    if (columns > 0 && rows > 0) {
        shellConnection?.resize(columns, rows)
    }
}

private const val DEFAULT_COLUMNS = 80
private const val DEFAULT_ROWS = 24
