package com.example.machina.ui.screens.dashboard.settings

import RequestNotificationPermission
import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.ui.theme.AppGreen


private val SettingsBackground = Color(0xFFF4F7F5)
private val SettingsText = Color(0xFF15221F)
private val SettingsMuted = Color(0xFF687874)
private val SettingsIconBg = Color(0xFFEAF7F1)
private val SettingsIconTint = Color(0xFF1E7C63)
private val SettingsCard = Color.White
private val SettingsDivider = Color(0xFFE8EDEA)

@Composable
fun SettingsScreen(
    @Suppress("UNUSED_PARAMETER") navController: NavController,
    onChangePasswordClick: () -> Unit = {},
    onRateAppClick: () -> Unit = {},
    onShareAppClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onTermsAndConditionsClick: () -> Unit = {},
    onContactClick: () -> Unit = {},
    onNotificationSettingsClick: () -> Unit = {},
) {
    RequestNotificationPermission()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SettingsBackground),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            SettingsHeader()
        }

        item {
            NotificationHeroCard(onNotificationSettingsClick = onNotificationSettingsClick)
        }

        item {
            SettingsSection(title = "Account") {
                SettingsRow(
                    icon = Icons.Outlined.Password,
                    title = "Change password",
                    subtitle = "Update your login credentials",
                    onClick = onChangePasswordClick,
                    showDivider = false
                )
            }
        }

        item {
            SettingsSection(title = "Preferences") {
                DarkModeRow()
            }
        }

        item {
            SettingsSection(title = "App") {
                SettingsRow(
                    icon = Icons.Outlined.StarBorder,
                    title = "Rate app",
                    subtitle = "Share feedback on the store",
                    onClick = onRateAppClick
                )
                SettingsRow(
                    icon = Icons.Outlined.Share,
                    title = "Share app",
                    subtitle = "Invite others to Machina",
                    onClick = onShareAppClick,
                    showDivider = false
                )
            }
        }

        item {
            SettingsSection(title = "Legal") {
                SettingsRow(
                    icon = Icons.Outlined.Lock,
                    title = "Privacy policy",
                    subtitle = "How we handle your data",
                    onClick = onPrivacyPolicyClick
                )
                SettingsRow(
                    icon = Icons.Outlined.Description,
                    title = "Terms and conditions",
                    subtitle = "Rules for using Machina",
                    onClick = onTermsAndConditionsClick,
                    showDivider = false
                )
            }
        }

        item {
            SettingsSection(title = "Support") {
                SettingsRow(
                    icon = Icons.Outlined.Email,
                    title = "Contact",
                    subtitle = "Email or call our team",
                    onClick = onContactClick,
                    showDivider = false
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SettingsHeader() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Settings",
            color = SettingsText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Manage notifications, security, and app preferences",
            color = SettingsMuted,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun NotificationHeroCard(onNotificationSettingsClick: () -> Unit = {}) {
    var notificationsEnabled by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF1E7C63),
                            AppGreen,
                            Color(0xFF7ED4AA)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Notifications",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (notificationsEnabled) {
                            "Stay updated on VM and cloud activity"
                        } else {
                            "Alerts are turned off"
                        },
                        color = Color.White.copy(alpha = 0.88f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }

                IconButton(
                    onClick = onNotificationSettingsClick,
                    modifier = Modifier.size(48.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title.uppercase(),
            color = SettingsMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = SettingsCard,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

@Composable
private fun DarkModeRow() {
    var darkModeEnabled by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SettingsIconBox(icon = Icons.Outlined.DarkMode)

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Dark mode",
                color = SettingsText,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Coming in a future update",
                color = SettingsMuted,
                fontSize = 13.sp
            )
        }

        Switch(
            checked = darkModeEnabled,
            onCheckedChange = { darkModeEnabled = it },
            enabled = false,
            colors = SwitchDefaults.colors(
                disabledCheckedThumbColor = Color.White,
                disabledCheckedTrackColor = AppGreen.copy(alpha = 0.5f),
                disabledUncheckedThumbColor = Color.White,
                disabledUncheckedTrackColor = Color(0xFFD5DED9)
            )
        )
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    showDivider: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(color = AppGreen.copy(alpha = 0.12f))
                ) { onClick() }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingsIconBox(icon = icon)

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = SettingsText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = SettingsMuted,
                    fontSize = 13.sp
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = SettingsMuted,
                modifier = Modifier.size(22.dp)
            )
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 72.dp),
                color = SettingsDivider,
                thickness = 1.dp
            )
        }
    }
}

@Composable
private fun SettingsIconBox(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(SettingsIconBg),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SettingsIconTint,
            modifier = Modifier.size(22.dp)
        )
    }
}
