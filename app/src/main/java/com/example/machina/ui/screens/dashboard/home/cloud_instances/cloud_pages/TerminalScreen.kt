import android.annotation.SuppressLint
import android.view.WindowInsets
import android.webkit.JavascriptInterface
import android.webkit.WebView
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.machina.view_model.dashboard_viewmodel.SshConnectionViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_pages.TerminalBridge
import com.example.machina.ui.theme.OnlineGreen
import com.example.machina.ui.theme.terminalChrome
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.BackButton
import com.example.machina.view_model.dashboard_viewmodel.SshConnectionUiState
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

    var webView by remember { mutableStateOf<WebView?>(null) }
    var bridge by remember { mutableStateOf<TerminalBridge?>(null) }

    fun leaveTerminal() {
        bridge?.stop()
        bridge = null
//        viewModel.closeInteractiveTerminal()
        navController.popBackStack()
    }

    BackHandler(onBack = ::leaveTerminal)

    LaunchedEffect(state) {
        if (state is SshConnectionUiState.Success) {
            val shell = withContext(Dispatchers.IO) {
                viewModel.openInteractiveShell()
            }
            val wv = webView ?: return@LaunchedEffect
            bridge = TerminalBridge(shell, wv).apply {
                start()
            }
        }
    }

    Scaffold(
//        contentWindowInsets = WindowInsets.CONSUMED
    ) { padding ->

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

            // TERMINAL WEBVIEW
            AndroidView(
                factory = { ctx ->

                    WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true

                        // JS → Android bridge
                        addJavascriptInterface(
                            object {
                                @JavascriptInterface
                                fun sendInput(data: String) {
                                    bridge?.sendInput(data)
                                }
                            },
                            "AndroidTerminal"
                        )

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