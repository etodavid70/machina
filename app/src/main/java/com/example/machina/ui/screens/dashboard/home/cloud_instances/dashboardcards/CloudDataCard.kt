package com.example.machina.ui.screens.dashboard.home.cloud_instances.dashboardcards
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.machina.data.model.dashboard_models.CloudInstances
import com.example.machina.ui.widgets.AppText

@Composable
fun CloudCard(vm: CloudInstances) {
    Card(
        modifier = Modifier
            .width(250.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AppText(text = vm.name,
            )
            AppText(text = "Status: ${vm.status}")

        }
    }
}


@Composable
fun CloudHorizontalList(vmList: List<CloudInstances>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp), // 👈 VERY IMPORTANT
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(vmList) { vm ->
            CloudCard(vm)
        }
    }
}