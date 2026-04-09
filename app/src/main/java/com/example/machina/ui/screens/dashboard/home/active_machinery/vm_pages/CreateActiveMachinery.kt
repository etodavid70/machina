package com.example.machina.ui.screens.dashboard.home.active_machinery.vm_pages

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.machina.ui.widgets.AppText

@Composable
fun CreateVirtualMachine(){


    Column(){
        AppText(text = "Create a Virtual Machine", fontWeight = FontWeight.Bold, fontSize = 25.sp)


    }
}