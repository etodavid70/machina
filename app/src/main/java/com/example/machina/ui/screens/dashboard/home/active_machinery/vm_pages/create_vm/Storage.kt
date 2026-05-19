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
fun SelectStorage(){

    var selectedStorage by remember { mutableStateOf(100f) }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally


    ){
        AppText("Storage", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        AppText("Adjust the meter to get the Storage size")

        Spacer(modifier = Modifier.height(16.dp))
        SemiCircularKnob(
            min = 50f,
            max = 500f,
            initialValue = selectedStorage,
            step = 10f,
            valueFormatter = { value -> "${value.toInt()} GB" }
        ) { newValue ->
            selectedStorage = newValue
        }

        AppText("Selected: ${selectedStorage.toInt()} GB")

        AppWhiteButton("Save", {})

        Spacer(modifier = Modifier.height(16.dp))

        AppButton("Confirm", {})
    }

}
