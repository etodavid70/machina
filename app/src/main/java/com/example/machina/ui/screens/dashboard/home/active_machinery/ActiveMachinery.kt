package com.example.machina.ui.screens.dashboard.home.active_machinery
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.machina.data.model.dashboard_models.ActiveMachinery
import com.example.machina.ui.screens.dashboard.home.widgets.NoDataCard
import com.example.machina.ui.screens.dashboard.home.widgets.VmDataCard
import com.example.machina.ui.theme.AppPurple
import com.example.machina.R


@Composable
fun ActiveMachineryCard(
    vmList: List<ActiveMachinery>,
    onCreateClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
    {
//        Log.d("vmList:", vmList.toString())
        if (vmList.isEmpty()) {

            NoDataCard(
                onButtonClick = onCreateClick,
                cardText = "You currently do not have an active machinery",
                buttonText = "Create a Vm",
                backgroundColor = AppPurple
            )
        } else {

            VmDataCard(
                onButtonClick = onCreateClick,
                cardText = "(${vmList.size}) Running Instances",
                buttonText = "View Active Machineries",
                backgroundColor = AppPurple,
                imageRes = R.drawable.machinery // or dynamic later
            )
        }
    }
    }
