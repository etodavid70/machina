package com.example.machina.ui.screens.dashboard.home.cloud_instances.dashboardcards

import com.example.machina.ui.screens.dashboard.home.widgets.NoDataCard
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.machina.ui.theme.AppOrange


@Composable
fun CloudInstancesCard(
    onCreateClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
    {

        NoDataCard(onCreateClick,
            "You currently do not  have a  cloud Instance",
            "Connect to an instance",
            AppOrange
        )

    }
}
