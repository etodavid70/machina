package com.example.machina.ui.widgets

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppFilePicker(
    mimeType: String = "*/*",
    onFilePicked: (Uri?) -> Unit,
    content: @Composable (onClick: () -> Unit) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onFilePicked(uri)
    }

    content {
        launcher.launch(mimeType)
    }
}

@Composable
fun AppDocumentPicker(
    mimeTypes: Array<String> = arrayOf(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "text/plain"
    ),
    onDocumentPicked: (Uri?) -> Unit,
    content: @Composable (onClick: () -> Unit) -> Unit
) {

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        onDocumentPicked(uri)
    }
    content {
        launcher.launch(mimeTypes)
    }
}