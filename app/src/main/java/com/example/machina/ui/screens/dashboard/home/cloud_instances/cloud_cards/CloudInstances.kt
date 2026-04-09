package com.example.machina.ui.screens.dashboard.home.cloud_instances.cloud_cards

import com.example.machina.ui.screens.dashboard.home.widgets.NoDataCard
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.machina.R
import com.example.machina.data.model.dashboard_models.CloudInstances
import com.example.machina.ui.screens.dashboard.home.widgets.VmDataCard
import com.example.machina.ui.theme.AppOrange


@Composable
fun CloudInstancesCard(
    cloudList: List<CloudInstances>,
    onCreateClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
    {
//        Log.d("cloudList:", cloudList.toString())
        if (cloudList.isEmpty()) {
            NoDataCard(
                onCreateClick,
                "You currently do not  have a  cloud Instance",
                "Connect to an instance",
                AppOrange
            )
        } else {

            VmDataCard(
                onButtonClick = onCreateClick,
                cardText = "(${cloudList.size}) Running Instances",
                buttonText = "View Instances",
                backgroundColor = AppOrange,
                imageRes = R.drawable.cloud_instances // or dynamic later
            )
        }
    }
}
