package com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_pages

import AppButton
import AppWhiteButton
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.R
import com.example.machina.data.model.dashboard_models.SshConnectionRequest
import com.example.machina.ui.navigation.Screen
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.AppDocumentPicker
import com.example.machina.ui.widgets.AppPasswordField
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.AppTextFieldRounded
import com.example.machina.ui.widgets.BackButton
import com.example.machina.view_model.dashboard_viewmodel.SshConnectionUiState
import com.example.machina.view_model.dashboard_viewmodel.SshConnectionViewModel
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState

@Composable
fun ConnectToACloudInstance(
    navController: NavController,
    viewModel: SshConnectionViewModel
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    var username by remember { mutableStateOf("") }
    var publicIp by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("2222") }
    var password by remember { mutableStateOf("") }
    var selectedFile by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val isLoading = state is SshConnectionUiState.Loading

    LaunchedEffect(state) {
        when (val currentState = state) {
            is SshConnectionUiState.Success -> {
                Log.d("connect", "connected: ${currentState.result.output}")
                snackbarHostState.showSnackbar(
                    message = "Connected successfully 🎉"
                )
                navController.navigate(Screen.Terminal.route)
            }

            is SshConnectionUiState.Error -> {
                Log.e("connect", "failed: ${currentState.message}")
                snackbarHostState.showSnackbar(
                    message = currentState.message
                )
            }

            SshConnectionUiState.Idle,
            SshConnectionUiState.Loading -> Unit
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )
    { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(
                    navController = navController,
//                onClick = TODO(),
                    modifier = Modifier
                )

                AppText(
                    "Connect to a cloud Instance",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
//                modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.cloud_instance_green),
                contentDescription = "Background Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(150.dp)
            )

            Spacer(Modifier.height(16.dp))

            AppTextFieldRounded(
                value = username,
                onValueChange = {
                    username = it
                    validationError = null
                    viewModel.resetState()
                },
                placeholder = "Username"
            )
            Spacer(Modifier.height(16.dp))

            AppTextFieldRounded(
                value = publicIp,
                onValueChange = {
                    publicIp = it
                    validationError = null
                    viewModel.resetState()
                },
                placeholder = "Public IP Address",
//            borderColor = AppGreen
            )
            Spacer(Modifier.height(16.dp))
            AppTextFieldRounded(
                value = port,
                onValueChange = {
                    port = it.filter(Char::isDigit)
                    validationError = null
                    viewModel.resetState()
                },
                placeholder = "SSH Port"
            )
            Spacer(Modifier.height(16.dp))
            AppPasswordField(
                value = password,
                onValueChange = {
                    password = it
                    validationError = null
                    viewModel.resetState()
                },
                placeholder = "Password",
                borderColor = AppGreen
            )
            Spacer(modifier = Modifier.height(16.dp))

            selectedFileName?.let {
                Text(text = "Selected: $it")
            }
            AppDocumentPicker(
                mimeTypes = arrayOf(
                    "*/*",
                    "application/x-pem-file",
                    "application/x-x509-ca-cert",
                    "text/plain"
                ),
                onDocumentPicked = { uri ->
                    selectedFile = uri
                    validationError = null
                    viewModel.resetState()
                    uri?.let {
                        selectedFileName = getFileName(context, it)
                    }
                }
            ) { onClick ->
                AppWhiteButton(
                    text = "Upload PEM key",
                    onClick = onClick

                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            //connect button
            AppButton(
                onClick = {
                    Log.d("connect", "validating ssh connection form")

                    val request = buildSshConnectionRequest(
                        context = context,
                        host = publicIp,
                        username = username,
                        port = port,
                        password = password,
                        privateKeyUri = selectedFile,
                        privateKeyName = selectedFileName
                    ).getOrElse { error ->
                        validationError = error.message
                            ?: "Enter username, public IP, port, and a password or PEM key."
                        Log.e("connect", "validation error: $validationError")
                        return@AppButton
                    }

                    validationError = null
                    Log.d(
                        "connect",
                        "connecting to ${request.username}@${request.host}:${request.port}"
                    )
                    viewModel.connect(request)
                },
                text = "Connect",
                isEnabled = !isLoading,
                isLoading = isLoading
            )

            Spacer(Modifier.height(16.dp))

            validationError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }

            when (val currentState = state) {
                is SshConnectionUiState.Error -> Text(
                    text = currentState.message,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )

                SshConnectionUiState.Idle -> Unit
                SshConnectionUiState.Loading -> CircularProgressIndicator(color = AppGreen)
                is SshConnectionUiState.Success -> Text(
                    text = "Connected: ${currentState.result.output}",
                    color = AppGreen,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun buildSshConnectionRequest(
    context: Context,
    host: String,
    username: String,
    port: String,
    password: String,
    privateKeyUri: Uri?,
    privateKeyName: String?
): Result<SshConnectionRequest> {
    return runCatching {
        val parsedPort = port.toIntOrNull()
            ?: throw IllegalArgumentException("Enter a valid SSH port.")

        val privateKey = privateKeyUri?.let { uri ->
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: throw IllegalArgumentException("Could not read the selected PEM key.")
        }

        SshConnectionRequest(
            host = host.trim(),
            username = username.trim(),
            port = parsedPort,
            password = password.takeIf { it.isNotBlank() },
            privateKey = privateKey,
            privateKeyName = privateKeyName
        )
    }
}

fun getFileName(context: Context, uri: Uri): String {
    var name = "Unknown file"

    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (it.moveToFirst() && nameIndex != -1) {
            name = it.getString(nameIndex)
        }
    }
    return name
}
