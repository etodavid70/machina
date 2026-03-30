package com.example.machina.ui.screens.dashboard.home.active_machinery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.machina.data.model.dashboard_models.ActiveMachinery
import com.example.machina.ui.widgets.AppText

@Composable

fun VmCard(vm: ActiveMachinery) {
    Card(
        modifier = Modifier
            .width(250.dp), // 👈 key change
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
fun VmHorizontalList(vmList: List<ActiveMachinery>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
//        items(vmList) { vm ->
//            VmCard(vm)
//        }
    }
}