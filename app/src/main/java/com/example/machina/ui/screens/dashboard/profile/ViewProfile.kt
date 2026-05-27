package com.example.machina.ui.screens.dashboard.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.R
import com.example.machina.ui.navigation.Screen
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.BackButton
import com.example.machina.utils.clearToken
import com.example.machina.utils.formatDate
import com.example.machina.view_model.dashboard_viewmodel.DashboardViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewProfileScreen(
    navController: NavController,
    viewModel: DashboardViewModel = koinViewModel(),
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val profile by viewModel.profile.collectAsState()
    val isLoading by viewModel.profileLoading.collectAsState()
    val errorMessage by viewModel.profileErrorMessage.collectAsState()
    val fullName = listOf(profile.firstName, profile.lastName)
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { "Your profile" }
    val firstName = profile.firstName.ifBlank { "Not set" }
    val lastName = profile.lastName.ifBlank { "Not set" }
    val dateOfBirth = profile.dob
        ?.takeIf { it.isNotBlank() }
        ?.let { formatDate(it) }
        ?: "Not set"
    val gender = profile.gender
        .ifBlank { "Not set" }
        .replaceFirstChar { it.uppercase() }

    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileBackground),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ProfileTopBar(
                navController = navController,
                onEditClick = { navController.navigate(Screen.EditProfile.route) }
            )
        }

        item {
            ProfileHero(
                fullName = fullName,
                gender = gender,
                onEditClick = { navController.navigate(Screen.EditProfile.route) }
            )
        }

        if (isLoading || errorMessage != null) {
            item {
                ProfileStatus(
                    isLoading = isLoading,
                    errorMessage = errorMessage
                )
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Personal details",
                        color = ProfileText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ProfileDetailRow(
                        icon = Icons.Default.Person,
                        label = "First name",
                        value = firstName
                    )
                    ProfileDetailRow(
                        icon = Icons.Default.Person,
                        label = "Last name",
                        value = lastName
                    )
                    ProfileDetailRow(
                        icon = Icons.Default.Cake,
                        label = "Date of birth",
                        value = dateOfBirth
                    )
                    ProfileDetailRow(
                        icon = Icons.Default.Face,
                        label = "Gender",
                        value = gender
                    )
                }
            }
        }

        item {
            LogoutButton(
                onClick = {
                    clearToken(context)
                    onLogout()
                }
            )
        }
    }
}

@Composable
private fun ProfileTopBar(
    navController: NavController,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(
            navController = navController,
            modifier = Modifier.size(44.dp)
        )
        Text(
            text = "Profile",
            modifier = Modifier.weight(1f),
            color = ProfileText,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )

    }
}

@Composable
private fun ProfileHero(
    fullName: String,
    gender: String,
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0E1F1B),
                        Color(0xFF1D4A3F)
                    )
                )
            )
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 46.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "Profile avatar",
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White.copy(alpha = 0.85f), CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = fullName,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Machina account",
                        color = Color(0xFFBFEEDB),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProfileChip(
                    icon = Icons.Default.VerifiedUser,
                    text = "Active profile",
                    modifier = Modifier.weight(1f)
                )
                ProfileChip(
                    icon = Icons.Default.Face,
                    text = gender,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.16f))
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit profile",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun ProfileChip(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFBFEEDB),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ProfileStatus(
    isLoading: Boolean,
    errorMessage: String?
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Box(
            modifier = Modifier.padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = AppGreen,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFB3261E),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun ProfileDetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEAF7F1)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF1E7C63),
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = ProfileMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = ProfileText,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun LogoutButton(
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFFE53935)),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFFE53935)
        )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Logout,
            contentDescription = "Logout",
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Logout",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private val ProfileBackground = Color(0xFFF4F7F5)
private val ProfileText = Color(0xFF15221F)
private val ProfileMuted = Color(0xFF687874)
