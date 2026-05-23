package com.example.machina.ui.screens.dashboard.settings

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.*

import androidx.compose.material3.*
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.machina.ui.theme.AppGreen
import com.example.machina.utils.clearToken


@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onRateAppClick: () -> Unit = {},
    onShareAppClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onTermsAndConditionsClick: () -> Unit = {},
    onContactClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 24.dp, vertical = 40.dp)
    ) {

        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            Text(
                text = "Settings",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Notification Card
        NotificationItem()

        Spacer(modifier = Modifier.height(24.dp))

        // Settings Items
        SettingsItem(
            icon = Icons.Outlined.LightMode,
            title = "Dark Mode"
        )


        SettingsItem(
            icon = Icons.Outlined.Password,
            title = "Change Password",
            onClick = onChangePasswordClick
        )
        SettingsItem(
            icon = Icons.Outlined.StarBorder,
            title = "Rate App",
            onClick = onRateAppClick
        )

        SettingsItem(
            icon = Icons.Outlined.Share,
            title = "Share App",
            onClick = onShareAppClick
        )

        SettingsItem(
            icon = Icons.Outlined.Lock,
            title = "Privacy Policy",
            onClick = onPrivacyPolicyClick
        )

        SettingsItem(
            icon = Icons.Outlined.Description,
            title = "Terms and Conditions",
            onClick = onTermsAndConditionsClick
        )

//        SettingsItem(
//            icon = Icons.Outlined.Description,
//            title = "Cookies Policy"
//        )

        SettingsItem(
            icon = Icons.Outlined.Email,
            title = "Contact",
            onClick = onContactClick
        )


        Spacer(modifier = Modifier.height(20.dp))

        // Logout
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    clearToken(context)
                    onLogout()
                }
                .padding(vertical = 14.dp)
        ) {

            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Logout,
                contentDescription = "Logout",
                tint = Color.Red,
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.width(18.dp))

            Text(
                text = "Logout",
                color = Color.Red,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun NotificationItem() {

    var checked by remember {
        mutableStateOf(true)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFA8DCC3))
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "Notification",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AppGreen
//                checkedTrackColor = Color(0xFF4CC38A)
            )
        )
    }
}


@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit = {}
) {

    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple()
            ) {
                onClick()
            }
            .padding(vertical = 18.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.Black,
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}
