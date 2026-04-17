import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.machina.utils.NotificationHelper

@Composable
fun rememberNotificationHandler(): (String, String) -> Unit {

    val context = LocalContext.current

    var pendingTitle by remember { mutableStateOf<String?>(null) }
    var pendingMessage by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->

        if (isGranted) {
            NotificationHelper.showNotification(
                context,
                pendingTitle ?: "",
                pendingMessage ?: ""
            )
        }
    }




    return { title: String, message: String ->

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                NotificationHelper.showNotification(context, title, message)

            } else {
                // Save values before requesting permission
                pendingTitle = title
                pendingMessage = message

                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }

        } else {
            NotificationHelper.showNotification(context, title, message)
        }
    }
}