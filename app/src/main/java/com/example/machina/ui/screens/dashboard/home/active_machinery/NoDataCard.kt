package com.example.machina.ui.screens.dashboard.home.active_machinery
import AppButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.ui.res.painterResource
import com.example.machina.R
import com.example.machina.ui.widgets.AppText

@Composable
fun NoDataCard(onCreateClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f), // makes it visually prominent
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.no_data),
                contentDescription = "Image",
            )



            AppText(
                text = "No Data",
//
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "You currently do not  have an active machinery",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                text = "Create a Vm",
                onClick = {}
            )

        }
    }
}