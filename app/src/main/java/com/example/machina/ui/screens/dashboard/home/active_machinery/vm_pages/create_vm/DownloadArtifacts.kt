package com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm

import AppButton
import android.R.attr.title
import com.example.machina.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.machina.ui.widgets.AppProgressBar
import com.example.machina.ui.widgets.AppText


@Composable
fun DownloadArtifacts() {

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        AppText(
            text = "Downloading Required Artifacts",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description
        AppText(
            text = "Please wait...",
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        // Image
        Image(
            painter = painterResource(id = R.drawable.machina),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        AppProgressBar(
            progress = 0.6F
        )


        Spacer(modifier = Modifier.height(30.dp))

        AppText(
            text = "You can minimize or close the app while downloading",
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )


    }
}