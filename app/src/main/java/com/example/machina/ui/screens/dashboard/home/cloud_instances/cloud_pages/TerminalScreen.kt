package com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_pages

import android.annotation.SuppressLint
import android.graphics.Color as AndroidColor
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebSettings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.machina.ui.theme.OnlineGreen
import com.example.machina.ui.theme.terminalChrome
import com.example.machina.view_model.dashboard_viewmodel.SshConnectionUiState
import com.example.machina.view_model.dashboard_viewmodel.SshConnectionViewModel
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.BackButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TerminalScreen(
    navController: NavController,
    viewModel: SshConnectionViewModel
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val connectedHost = (state as? SshConnectionUiState.Success)?.result?.let {
        "${it.username}@${it.host}:${it.port}"
    } ?: "No active shell"

    // Memoize WebView and bridge to prevent recreating on recomposition
    var webView by remember { mutableStateOf<WebView?>(null) }

    //initializes the the js/kotlin bridge which is called in the Launch Effect
    var bridge by remember { mutableStateOf<TerminalBridge?>(null) }

    fun leaveTerminal() {
        bridge?.stop()
        bridge = null
        webView?.destroy()
        webView = null
        navController.popBackStack()
    }

    BackHandler(onBack = ::leaveTerminal)

    //Eto: Launches the shell when the ssh connection is established
    LaunchedEffect(state) {

        //Eto: If the connection is successful
        if (state is SshConnectionUiState.Success) {
            //open the shell
            val shell = withContext(Dispatchers.IO) {

                //Eto: calls the function in the viewmodel
                //to establish ssh connectivity
                viewModel.openInteractiveShell()
            }
            //Eto: If webview is null dont continue this launch effect
            val wv = webView ?: return@LaunchedEffect
            
            // Eto: Stop any existing bridge
            bridge?.stop()

            //Eto: creates a js-kotlin bridge using the file TerminalBridge.kt
            bridge = TerminalBridge(shell, wv).apply {
                start()
            }
        }
    }

    //Eto: Cleanup on dispose to avoid memory leak
    DisposableEffect(Unit) {
        onDispose {
            //Eto: stops the bridge
            bridge?.stop()

            //Eto: destroys the webview
            webView?.destroy()
        }
    }

    Scaffold {
        padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // TOP BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(terminalChrome)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(
                    onClick = ::leaveTerminal,
                    modifier = Modifier
                )
                Column(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .weight(1f)
                ) {
                    AppText(
                        text = "SSH Terminal",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    AppText(
                        text = connectedHost,
                        fontSize = 12.sp,
                        color = Color(0xFF8EA39B),
                        maxLines = 1
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                }
            }

            // Eto: Webview that loads index.html
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        // Eto: Performance settings
                        settings.apply {

                            //Eto: enable javascript
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            databaseEnabled = false
                            
                            // Rendering optimizations
                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            builtInZoomControls = false
                            displayZoomControls = false
                            useWideViewPort = false
                            loadWithOverviewMode = false
                            setSupportZoom(false)
                            
                            // Caching optimizations
                            cacheMode = WebSettings.LOAD_DEFAULT
                            
                            // Text scaling (terminal font)
                            defaultFontSize = 14
                            textZoom = 100
                            
                            // Disable unneeded features
//                            geolocationEnabled = false
                            setGeolocationDatabasePath(null)
                            mediaPlaybackRequiresUserGesture = true
                        }
                        
                        // Rendering hints
                        setBackgroundColor(AndroidColor.BLACK)
                        setLayerType(WebView.LAYER_TYPE_HARDWARE, null)  // Hardware acceleration
                        
                        //Eto: JS → Android bridge (input)
                        addJavascriptInterface(
                            object {
                                @JavascriptInterface
                                fun sendInput(data: String) {
                                    bridge?.sendInput(data)
                                }
                            },
                            "AndroidTerminal"
                        )

                        //the webview loads the index.html
                        //the index.html
                        loadUrl("file:///android_asset/terminal/index.html")
                        webView = this
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
    }
}
