package com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages.create_vm


import AppButton
import AppWhiteButton
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.machina.ui.screens.dashboard.home.active_machinery.widgetst.SemiCircularKnob
import com.example.machina.ui.widgets.AppText


@Composable
fun SelectCPUCores(){

    var selectedCpuCores by remember { mutableStateOf(2f) }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally


    ){
        AppText("Processor", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        AppText("Adjust the meter to get the number of CPU cores")

        Spacer(modifier = Modifier.height(16.dp))
        SemiCircularKnob(
            min = 1f,
            max = 5f,
            initialValue = selectedCpuCores,
            step = 1f,
            valueFormatter = { value -> "${value.toInt()} cores" }
        ) { newValue ->
            selectedCpuCores = newValue
        }

        AppText("Selected: ${selectedCpuCores.toInt()} cores")

        AppWhiteButton("Save", {})

        Spacer(modifier = Modifier.height(16.dp))

        AppButton("Confirm", {})
    }

}
