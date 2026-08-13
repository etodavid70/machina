package com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_pages

import AppButton
import AppWhiteButton
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.R
import com.example.machina.data.model.dashboard_models.SavedServer
import com.example.machina.data.model.dashboard_models.ServerInstance
import com.example.machina.data.model.dashboard_models.SshConnectionRequest
import com.example.machina.ui.navigation.Screen
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.theme.AppGreenLight
import com.example.machina.ui.theme.AppGrey
import com.example.machina.ui.widgets.AppDocumentPicker
import com.example.machina.ui.widgets.AppPasswordField
import com.example.machina.ui.widgets.AppTextFieldRounded
import com.example.machina.ui.widgets.BackButton
import com.example.machina.view_model.dashboard_viewmodel.DashboardUiState
import com.example.machina.view_model.dashboard_viewmodel.DashboardViewModel
import com.example.machina.view_model.dashboard_viewmodel.SshConnectionUiState
import com.example.machina.view_model.dashboard_viewmodel.SshConnectionViewModel
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.filled.Link

@Composable
fun ConnectToACloudInstance(
    navController: NavController,
    viewModel: SshConnectionViewModel,
    dashboardViewModel: DashboardViewModel,
    savedServer: ServerInstance? = null,
    savedServerMode: Boolean = false
) {
    if (savedServerMode && savedServer == null) {
        MissingSavedServer(navController = navController)
        return
    }

    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val dashboardState by dashboardViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val serverKey = savedServer?.id

    var username by remember(serverKey) { mutableStateOf(savedServer?.username.orEmpty()) }
    var publicIp by remember(serverKey) { mutableStateOf(savedServer?.publicIp.orEmpty()) }
    var port by remember(serverKey) { mutableStateOf(savedServer?.port?.toString() ?: "22") }
    var password by remember(serverKey) { mutableStateOf("") }
    var keyPassphrase by remember(serverKey) { mutableStateOf("") }
    var selectedFile by remember(serverKey) { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember(serverKey) { mutableStateOf<String?>(null) }
    var authMethod by remember(serverKey) { mutableStateOf(AuthMethod.PemKey) }
    var hasSubmitted by remember(serverKey) { mutableStateOf(false) }
    var pendingAuthMethod by remember(serverKey) { mutableStateOf(AuthMethod.PemKey) }
    var connectedRequest by remember(serverKey) { mutableStateOf<SshConnectionRequest?>(null) }
    var connectedAuthMethod by remember(serverKey) { mutableStateOf(AuthMethod.PemKey) }
    var showSavePrompt by remember(serverKey) { mutableStateOf(false) }
    var saveRequested by remember(serverKey) { mutableStateOf(false) }
    var serverName by remember(serverKey) { mutableStateOf("") }
    var serviceProvider by remember(serverKey) { mutableStateOf("AWS") }
    var serverNameError by remember(serverKey) { mutableStateOf<String?>(null) }
    var serviceProviderError by remember(serverKey) { mutableStateOf<String?>(null) }
    var validationError by remember(serverKey) { mutableStateOf<String?>(null) }
    var authError by remember(serverKey) { mutableStateOf<String?>(null) }
    var usernameError by remember(serverKey) { mutableStateOf<String?>(null) }
    var publicIpError by remember(serverKey) { mutableStateOf<String?>(null) }
    var portError by remember(serverKey) { mutableStateOf<String?>(null) }

    val isSavedServer = savedServer != null
    val isLoading = state is SshConnectionUiState.Loading

    LaunchedEffect(serverKey, savedServerMode) {
        viewModel.resetState()
        dashboardViewModel.resetState()
    }

    LaunchedEffect(state, hasSubmitted) {
        when (val currentState = state) {
            is SshConnectionUiState.Success -> {
                if (hasSubmitted) {
                    Log.d("connect", "connected: ${currentState.result.output}")
                    hasSubmitted = false
                    if (isSavedServer) {
                        navController.navigate(Screen.Terminal.route)
                    } else {
                        val result = currentState.result
                        connectedRequest =
                            viewModel.getActiveConnectionRequest() ?: SshConnectionRequest(
                                host = result.host,
                                username = result.username,
                                port = result.port,
                                password = "connected"
                            )
                        connectedAuthMethod = pendingAuthMethod
                        serverName = defaultServerName(result.username, result.host)
                        serviceProvider = "AWS"
                        showSavePrompt = true
                    }
                }
            }

            is SshConnectionUiState.Error -> {
                if (hasSubmitted) {
                    Log.e("connect", "failed: ${currentState.message}")
                    snackbarHostState.showSnackbar(message = currentState.message)
                }
            }

            SshConnectionUiState.Idle,
            SshConnectionUiState.Loading -> Unit
        }
    }

    LaunchedEffect(dashboardState, saveRequested) {
        when (val currentState = dashboardState) {
            is DashboardUiState.Success -> {
                if (saveRequested) {
                    saveRequested = false
                    showSavePrompt = false
                    dashboardViewModel.fetchInstances()
                    dashboardViewModel.resetState()
                    navController.navigate(Screen.Terminal.route)
                }
            }

            is DashboardUiState.Error -> {
                if (saveRequested) {
                    saveRequested = false
                    snackbarHostState.showSnackbar(currentState.message)
                    dashboardViewModel.resetState()
                }
            }

            DashboardUiState.Idle,
            DashboardUiState.Loading -> Unit
        }
    }

    Scaffold(
        containerColor = AppGrey,
        contentWindowInsets = WindowInsets(0.dp),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppGrey)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 16.dp,
//                    vertical = 12.dp
                ),
            verticalArrangement = Arrangement.Top
        ) {
            connectHeader(
                navController = navController,
                title = if (isSavedServer) "Authenticate server" else "Connect to cloud"
            )

            Spacer(Modifier.height(16.dp))

            if (isSavedServer) {
                savedServer?.let { server ->
                    SavedServerSummary(server = server)
                    Spacer(Modifier.height(14.dp))
                }
            } else {
                ManualConnectionIntro()
                Spacer(Modifier.height(16.dp))
                ManualConnectionFields(
                    username = username,
                    onUsernameChange = {
                        username = it
                        usernameError = null
                        validationError = null
                        viewModel.resetState()
                    },
                    usernameError = usernameError,
                    publicIp = publicIp,
                    onPublicIpChange = {
                        publicIp = it
                        publicIpError = null
                        validationError = null
                        viewModel.resetState()
                    },
                    publicIpError = publicIpError,
                    port = port,
                    onPortChange = {
                        port = it.filter(Char::isDigit)
                        portError = null
                        validationError = null
                        viewModel.resetState()
                    },
                    portError = portError
                )
                Spacer(Modifier.height(14.dp))
            }

            AuthenticationCard(
                authMethod = authMethod,
                onAuthMethodChange = {
                    authMethod = it
                    authError = null
                    validationError = null
                    viewModel.resetState()
                },
                password = password,
                onPasswordChange = {
                    password = it
                    authError = null
                    validationError = null
                    viewModel.resetState()
                },
                keyPassphrase = keyPassphrase,
                onKeyPassphraseChange = {
                    keyPassphrase = it
                    validationError = null
                    viewModel.resetState()
                },
                selectedFileName = selectedFileName,
                onDocumentPicked = { uri ->
                    selectedFile = uri
                    authError = null
                    validationError = null
                    viewModel.resetState()
                    selectedFileName = uri?.let { getFileName(context, it) }
                },
                authError = authError
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppButton(

                icon = Icons.Filled.Link,
                onClick = {
                    Log.d("connect", "validating ssh connection form")

                    val connectionUsername = savedServer?.username ?: username
                    val connectionHost = savedServer?.publicIp ?: publicIp
                    val connectionPort = savedServer?.port?.toString() ?: port
                    val parsedPort = connectionPort.toIntOrNull()

                    usernameError = if (!isSavedServer && connectionUsername.trim().isBlank()) {
                        "Username is required."
                    } else {
                        null
                    }
                    publicIpError = if (!isSavedServer && connectionHost.trim().isBlank()) {
                        "Public IP address is required."
                    } else {
                        null
                    }
                    portError = when {
                        isSavedServer -> null
                        connectionPort.isBlank() -> "SSH port is required."
                        parsedPort == null -> "Enter a valid SSH port."
                        parsedPort !in 1..65535 -> "SSH port must be between 1 and 65535."
                        else -> null
                    }
                    authError = when (authMethod) {
                        AuthMethod.PemKey -> if (selectedFile == null) "Upload a PEM key." else null
                        AuthMethod.Password -> if (password.isBlank()) "Password is required." else null
                    }

                    if (
                        usernameError != null ||
                        publicIpError != null ||
                        portError != null ||
                        authError != null
                    ) {
                        validationError = null
                        return@AppButton
                    }

                    val request = buildSshConnectionRequest(
                        context = context,
                        host = connectionHost,
                        username = connectionUsername,
                        port = connectionPort,
                        password = when (authMethod) {
                            AuthMethod.PemKey -> keyPassphrase
                            AuthMethod.Password -> password
                        },
                        privateKeyUri = if (authMethod == AuthMethod.PemKey) selectedFile else null,
                        privateKeyName = if (authMethod == AuthMethod.PemKey) selectedFileName else null
                    ).getOrElse { error ->
                        validationError = error.message
                            ?: "Enter server details and authentication credentials."
                        Log.e("connect", "validation error: $validationError")
                        return@AppButton
                    }

                    validationError = null
                    hasSubmitted = true
                    Log.d(
                        "connect",
                        "connecting to ${request.username}@${request.host}:${request.port}"
                    )
                    pendingAuthMethod = authMethod
                    viewModel.connect(request)
                },
                text = "Connect",
                isEnabled = !isLoading,
                isLoading = isLoading
            )

            validationError?.let {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = it,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(18.dp))
        }
    }

    connectedRequest?.let { request ->
        if (showSavePrompt) {
            SaveServerPromptDialog(
                request = request,
                authMethod = connectedAuthMethod,
                serverName = serverName,
                onServerNameChange = {
                    serverName = it
                    serverNameError = null
                },
                serverNameError = serverNameError,
                serviceProvider = serviceProvider,
                onServiceProviderChange = {
                    serviceProvider = it
                    serviceProviderError = null
                },
                serviceProviderError = serviceProviderError,
                isSaving = dashboardState is DashboardUiState.Loading && saveRequested,
                onDiscard = {
                    showSavePrompt = false
                    dashboardViewModel.resetState()
                    navController.navigate(Screen.Terminal.route)
                },


                onSave = {
                    // 1. Create local validation variables
                    val nameToSave = serverName.trim()
                    val providerToSave = serviceProvider.trim()

                    val nameIsInvalid = nameToSave.isBlank()
                    val providerIsInvalid = providerToSave.isBlank()

                    // 2. Update UI states for error showing
                    serverNameError = if (nameIsInvalid) "Server name is required." else null
                    serviceProviderError =
                        if (providerIsInvalid) "Service provider is required." else null

                    Log.d("save", "Validated: Name='$nameToSave', Provider='$providerToSave'")

                    // 3. Use the local booleans for the logic check, NOT the state variables
                    if (nameIsInvalid || providerIsInvalid) {
                        Log.d("save", "Validation failed, stopping execution.")
                        return@SaveServerPromptDialog
                    }

                    // 4. If we reached here, validation PASSED

                    saveRequested = true
                    dashboardViewModel.saveCloudInstance(
                        buildSavedServerPayload(
                            request = request,
                            authMethod = connectedAuthMethod,
                            name = nameToSave,
                            serviceProvider = providerToSave
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun connectHeader(
    navController: NavController,
    title: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(
            navController = navController,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
private fun ManualConnectionIntro() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.cloud_instance_green),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(132.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Remote SSH server",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ManualConnectionFields(
    username: String,
    onUsernameChange: (String) -> Unit,
    usernameError: String?,
    publicIp: String,
    onPublicIpChange: (String) -> Unit,
    publicIpError: String?,
    port: String,
    onPortChange: (String) -> Unit,
    portError: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppTextFieldRounded(
                value = username,
                onValueChange = onUsernameChange,
                placeholder = "Username",
                errorText = usernameError,
                helperText = "For EC2, use the AMI login user, such as ubuntu or ec2-user."
            )
            AppTextFieldRounded(
                value = publicIp,
                onValueChange = onPublicIpChange,
                placeholder = "Public IP Address",
                errorText = publicIpError
            )
            AppTextFieldRounded(
                value = port,
                onValueChange = onPortChange,
                placeholder = "SSH Port",
                errorText = portError
            )
        }
    }
}

@Composable
private fun SavedServerSummary(server: ServerInstance) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE7E8EA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(AppGreen.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = null,
                    tint = AppGreen
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = server.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${server.username}@${server.publicIp}:${server.port}",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = server.serviceProvider,
                    fontSize = 12.sp,
                    color = AppGreenLight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun AuthenticationCard(
    authMethod: AuthMethod,
    onAuthMethodChange: (AuthMethod) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    keyPassphrase: String,
    onKeyPassphraseChange: (String) -> Unit,
    selectedFileName: String?,
    onDocumentPicked: (Uri?) -> Unit,
    authError: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Key,
                    contentDescription = null,
                    tint = AppGreen,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Authentication",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AuthMethodButton(
                    text = "PEM key",
                    icon = Icons.Default.UploadFile,
                    selected = authMethod == AuthMethod.PemKey,
                    onClick = { onAuthMethodChange(AuthMethod.PemKey) },
                    modifier = Modifier.weight(1f)
                )
                AuthMethodButton(
                    text = "Password",
                    icon = Icons.Default.Lock,
                    selected = authMethod == AuthMethod.Password,
                    onClick = { onAuthMethodChange(AuthMethod.Password) },
                    modifier = Modifier.weight(1f)
                )
            }

            if (authMethod == AuthMethod.PemKey) {
                selectedFileName?.let {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = AppGreen.copy(alpha = 0.08f)
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(12.dp),
                            color = Color.DarkGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                AppDocumentPicker(
                    mimeTypes = arrayOf(
                        "*/*",
                        "application/x-pem-file",
                        "application/x-x509-ca-cert",
                        "text/plain"
                    ),
                    onDocumentPicked = onDocumentPicked
                ) { onClick ->
                    AppWhiteButton(
                        text = if (selectedFileName == null) "Choose PEM key" else "Change PEM key",
                        onClick = onClick
                    )
                }

                AppPasswordField(
                    value = keyPassphrase,
                    onValueChange = onKeyPassphraseChange,
                    placeholder = "Key passphrase (optional)",
                    borderColor = AppGreen
                )
            } else {
                AppPasswordField(
                    value = password,
                    onValueChange = onPasswordChange,
                    placeholder = "Server password",
                    borderColor = AppGreen,
                    errorText = authError
                )
            }

            if (authMethod == AuthMethod.PemKey) {
                authError?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthMethodButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, if (selected) AppGreen else Color.LightGray),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) AppGreen else Color.White,
            contentColor = if (selected) Color.White else Color.DarkGray
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SaveServerPromptDialog(
    request: SshConnectionRequest,
    authMethod: AuthMethod,
    serverName: String,
    onServerNameChange: (String) -> Unit,
    serverNameError: String?,
    serviceProvider: String,
    onServiceProviderChange: (String) -> Unit,
    serviceProviderError: String?,
    isSaving: Boolean,
    onDiscard: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            if (!isSaving) {
                onDiscard()
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = null,
                tint = AppGreen
            )
        },
        title = {
            Text(
                text = "Save this server?",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "${request.username}@${request.host}:${request.port}",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                AppTextFieldRounded(
                    value = serverName,
                    onValueChange = onServerNameChange,
                    placeholder = "Server name",
                    errorText = serverNameError
                )
                AppTextFieldRounded(
                    value = serviceProvider,
                    onValueChange = onServiceProviderChange,
                    placeholder = "Service provider",
                    errorText = serviceProviderError,
                    helperText = "For example AWS, Azure, or DigitalOcean."
                )
                Text(
                    text = "Authentication will be saved as ${authMethod.label}. The password or PEM key is not sent to the backend.",
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onSave,
                enabled = !isSaving,
                colors = ButtonDefaults.buttonColors(containerColor = AppGreen)
            ) {
                Text(
                    text = if (isSaving) "Saving..." else "Save",
                    color = Color.White
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDiscard,
                enabled = !isSaving
            ) {
                Text(
                    text = "Discard",
                    color = Color.Gray
                )
            }
        },
        containerColor = Color.White
    )
}

@Composable
private fun MissingSavedServer(navController: NavController) {
    Scaffold(
        containerColor = AppGrey,
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppGrey)
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            connectHeader(
                navController = navController,
                title = "Select a server"
            )
            Spacer(modifier = Modifier.height(48.dp))
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = null,
                tint = AppGreen,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No saved server selected.",
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(18.dp))
            AppButton(
                text = "Back to instances",
                onClick = { navController.popBackStack() }
            )
        }
    }
}

private fun buildSavedServerPayload(
    request: SshConnectionRequest,
    authMethod: AuthMethod,
    name: String,
    serviceProvider: String
): SavedServer {
    return SavedServer(
        name = name.trim(),
        connectionType = authMethod.connectionType,
        username = request.username.trim(),
        port = request.port,
        publicIp = request.host.trim(),
        privateIp = null,
        serviceProvider = serviceProvider.trim(),
        lastConnectedAt = null
    )
}

private fun defaultServerName(username: String, host: String): String {
    return "${username.trim()}@${host.trim()}"
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

private enum class AuthMethod {
    PemKey,
    Password;

    val connectionType: String
        get() = when (this) {
            PemKey -> "ssh_key"
            Password -> "password"
        }

    val label: String
        get() = when (this) {
            PemKey -> "SSH key"
            Password -> "password"
        }
}
