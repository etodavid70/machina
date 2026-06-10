package com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_pages

import AppButton
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.R
import com.example.machina.data.model.dashboard_models.ServerInstance
import com.example.machina.ui.navigation.Screen
import com.example.machina.ui.screens.dashboard.home.widgets.DeleteConfirmationDialog
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.theme.AppGreenLight
import com.example.machina.ui.theme.AppGrey
import com.example.machina.ui.theme.AppOrange
import com.example.machina.ui.theme.ConnectToANewSer
import com.example.machina.ui.theme.DeleteColor1
import com.example.machina.ui.theme.DeleteColor2
import com.example.machina.ui.theme.DeleteColor3
import com.example.machina.ui.theme.cloudInstanceHeaderSsh
import com.example.machina.ui.theme.cloudInstanceHeaderText
import com.example.machina.ui.theme.noCloudInstance
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.BackButton
import com.example.machina.view_model.dashboard_viewmodel.DashboardUiState
import com.example.machina.view_model.dashboard_viewmodel.DashboardViewModel

@Composable
fun ViewCloudInstance(
    navController: NavController,
    viewModel: DashboardViewModel,
) {
    val cloudList by viewModel.instances.collectAsState()

val context= LocalContext.current

    val deleteState by viewModel.deleteState.collectAsState()
    val isDeleting = deleteState is DashboardUiState.Loading

    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showDeleteDialog by remember { mutableStateOf(false) }

    var selectedInstance by remember {
        mutableStateOf<ServerInstance?>(null)
    }


    LaunchedEffect(Unit) {
        viewModel.fetchInstances()
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }


    LaunchedEffect(deleteState) {
        when (deleteState) {
            is DashboardUiState.Success -> {
                showDeleteDialog = false
                 viewModel.resetState()


                Toast.makeText(
                    context,
                    (deleteState as DashboardUiState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is DashboardUiState.Error -> {
                showDeleteDialog = false

                Toast.makeText(
                    context,
                    (deleteState as DashboardUiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }

    Scaffold(
        containerColor = AppGrey,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppGrey)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top
        ) {
            CloudInstancesHeader(
                navController = navController,
                instanceCount = cloudList.size
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 10.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when {
                    isLoading -> {
                        item {
                            LoadingInstances()
                        }
                    }

                    cloudList.isEmpty() -> {
                        item {
                            EmptyInstances(
                                onCreateClick = {
                                    navController.navigate(Screen.ConnectCloud.route)
                                }
                            )
                        }
                    }

                    else -> {
                        items(
                            items = cloudList,
                            key = { it.id }
                        ) { instance ->
                            SavedCloudInstanceCard(
                                instance = instance,
                                onConnectClick = {
                                    viewModel.selectInstance(instance)
                                    navController.navigate(Screen.ConnectSavedCloud.route)
                                },
                                onDeleteClick = {
                                    selectedInstance = instance
                                    showDeleteDialog = true

                                }
                            )
                        }

                        item {
                            AppButton(
                                text = ConnectToANewSer,
                                onClick = {
                                    viewModel.clearSelectedInstance()
                                    navController.navigate(Screen.ConnectCloud.route)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    //delete dialog box

    DeleteConfirmationDialog(
        showDialog = showDeleteDialog,
        onDelete = {
            selectedInstance?.let {
                viewModel.deleteInstance(it.id)
            }
        },
        onCancel = {
            showDeleteDialog = false
        },
        //this is for delete button
        isEnabled = !isDeleting,
        isLoading = isDeleting,
    )

}


//modal



@Composable
private fun CloudInstancesHeader(
    navController: NavController,
    instanceCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(
                navController = navController,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Cloud instances",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "$instanceCount saved server${if (instanceCount == 1) "" else "s"}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = AppOrange
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Cloud,
                        contentDescription = null,
                        tint = AppGreen
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cloudInstanceHeaderSsh,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Text(
                        text = cloudInstanceHeaderText,
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}


//loader
@Composable
private fun LoadingInstances() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = AppGreen,
            modifier = Modifier.size(30.dp),
            strokeWidth = 2.dp
        )
    }
}

//empty instance
@Composable
private fun EmptyInstances(
    onCreateClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.no_data),
                contentDescription = null,
                modifier = Modifier.size(88.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            AppText(
                text = noCloudInstance,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(
                text = "Connect a server",
                onClick = onCreateClick
            )
        }
    }
}

@Composable
private fun SavedCloudInstanceCard(
    instance: ServerInstance,
    onConnectClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE7E8EA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(AppGreen.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = null,
                        tint = AppGreen
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = instance.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${instance.username}@${instance.publicIp}",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InstanceMetadataPill(
                    icon = Icons.Default.Public,
                    text = instance.serviceProvider,
                    modifier = Modifier.weight(1f)
                )
                InstanceMetadataPill(
                    icon = Icons.Default.Key,
                    text = "Port ${instance.port}",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            //connect and delete buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onConnectClick,
//                modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppGreen)
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Connect", color = Color.White)
                }

                Button(
                    onClick = onDeleteClick,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeleteColor2)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Delete", color = Color.White)
                }

            }
        }
    }
}

@Composable
private fun InstanceMetadataPill(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = AppGreenLight.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppGreenLight,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                color = Color.DarkGray,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
