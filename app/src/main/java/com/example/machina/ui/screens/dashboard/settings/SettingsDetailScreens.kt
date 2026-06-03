package com.example.machina.ui.screens.dashboard.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.AppText

private val DetailBackground = Color(0xFFF4F7F5)
private val DetailText = Color(0xFF15221F)
private val DetailMuted = Color(0xFF687874)
private val DetailIconBg = Color(0xFFEAF7F1)
private val DetailIconTint = Color(0xFF1E7C63)

@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit = {}
) {
    SettingsDetailScaffold(
        title = "Privacy Policy",
        subtitle = "How Machina protects your information",
        onBackClick = onBackClick
    ) {
        InfoCard {
            SectionTitle("Your Privacy")
            BodyText("Machina respects your privacy and is committed to keeping your account and device information protected.")
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("Information We Use")
            BodyText("We may use basic account details, authentication information, and device information to provide secure access to app features and improve your experience.")
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("How We Protect Data")
            BodyText("We use reasonable security practices to help protect your information from unauthorized access, misuse, or disclosure.")
        }
    }
}

@Composable
fun TermsAndConditionsScreen(
    onBackClick: () -> Unit = {}
) {
    SettingsDetailScaffold(
        title = "Terms and Conditions",
        subtitle = "Guidelines for using the app",
        onBackClick = onBackClick
    ) {
        InfoCard {
            SectionTitle("Use of Machina")
            BodyText("By using Machina, you agree to use the app responsibly and only for lawful purposes.")
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("Account Responsibility")
            BodyText("You are responsible for keeping your login details secure and for activities performed through your account.")
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("Service Updates")
            BodyText("Features may be improved, changed, or temporarily unavailable as we maintain and upgrade the app.")
        }
    }
}

@Composable
fun ContactScreen(
    onBackClick: () -> Unit = {}
) {
    SettingsDetailScaffold(
        title = "Contact",
        subtitle = "We're here to help",
        onBackClick = onBackClick
    ) {
        InfoCard {
            SectionTitle("Need help?")
            BodyText("Reach out to the Machina support team through any of the channels below.")
            Spacer(modifier = Modifier.height(20.dp))
            ContactRow(
                icon = Icons.Outlined.Email,
                label = "Email",
                value = "admin@etotronics.com"
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = Color(0xFFE8EDEA)
            )
            ContactRow(
                icon = Icons.Outlined.Phone,
                label = "Phone",
                value = "+2347056192639"
            )
        }
    }
}

@Composable
fun UnavailableFeatureScreen(
    title: String,
    onBackClick: () -> Unit = {}
) {
    SettingsDetailScaffold(
        title = title,
        subtitle = "This feature is on the way",
        onBackClick = onBackClick
    ) {
        InfoCard(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier.size(72.dp),
                shape = RoundedCornerShape(20.dp),
                color = DetailIconBg
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = AppGreen,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            AppText(
                text = "Coming soon",
                color = DetailText,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            BodyText(
                text = "This feature is not available yet. Please check back in a future update.",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SettingsDetailScaffold(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DetailBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = DetailText
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = DetailText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = DetailMuted,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        content()
    }
}

@Composable
private fun InfoCard(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = horizontalAlignment,
            content = content
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = DetailText,
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
private fun BodyText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        color = DetailMuted,
        fontSize = 15.sp,
        lineHeight = 22.sp
    )
}

@Composable
private fun ContactRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(44.dp),
            shape = RoundedCornerShape(12.dp),
            color = DetailIconBg
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DetailIconTint,
                modifier = Modifier.padding(10.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = label,
                color = DetailMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                color = DetailText,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
