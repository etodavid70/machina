package com.example.machina.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.machina.R
import com.example.machina.utils.OsImageUrl

@DrawableRes
fun osPlaceholderDrawable(osName: String): Int = when (osName.lowercase()) {
    "windows" -> R.drawable.windows
    "linux", "ubuntu", "fedora", "kali", "redhat", "debian", "centos" -> R.drawable.linux
    else -> R.drawable.os
}

@Composable
fun OsAsyncImage(
    imageUrl: String,
    osName: String,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    contentDescription: String? = null,
) {
    val context = LocalContext.current
    val placeholderRes = osPlaceholderDrawable(osName)

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context)
            .data(OsImageUrl.resolve(imageUrl))
            .crossfade(true)
            .build(),
        contentDescription = contentDescription ?: osName,
        modifier = modifier.size(size),
        contentScale = ContentScale.Fit,
        loading = {
            Image(
                painter = painterResource(placeholderRes),
                contentDescription = null,
                modifier = Modifier.size(size),
                contentScale = ContentScale.Fit,
            )
        },
        error = {
            Image(
                painter = painterResource(placeholderRes),
                contentDescription = null,
                modifier = Modifier.size(size),
                contentScale = ContentScale.Fit,
            )
        },
    )
}
