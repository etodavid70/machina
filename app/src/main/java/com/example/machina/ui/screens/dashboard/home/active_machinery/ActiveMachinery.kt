package com.example.machina.ui.screens.dashboard.home.active_machinery
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.machina.data.model.dashboard_models.ActiveMachinery
import com.example.machina.ui.screens.dashboard.home.widgets.NoDataCard
import com.example.machina.ui.theme.AppPurple


@Composable
fun ActiveMachineryCard(
    onCreateClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
    {

        NoDataCard(
            onCreateClick,
            "You currently do not  have an active machinery",
            "Create a Vm",
            AppPurple
        )

    }
}