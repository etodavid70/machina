import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import android.util.Log
import androidx.compose.material3.TextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.R
import com.example.machina.data.model.onboarding_models.ProfileRequest
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.IndicatorUi
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.AppTextField
import com.example.machina.utils.getUserId
import com.example.machina.view_model.auth_viewmodel.AuthStep
import com.example.machina.view_model.auth_viewmodel.AuthUiState
import com.example.machina.view_model.auth_viewmodel.AuthViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    currentPage: Int = 2,
    totalPages: Int = 4,
    viewModel: AuthViewModel = koinViewModel()
) {

    val context = LocalContext.current
    val userId = remember { getUserId(context) }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showGenderDropdown by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val dateFormatter = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }
    val genderOptions = listOf("Male", "Female")

    LaunchedEffect(Unit) {
        viewModel.state.collectLatest { state ->
            if (state is AuthUiState.Success && state.step == AuthStep.ProfileSubmitted) {
                viewModel.resetState()
                navController.navigate("password")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {



        IndicatorUi(
            currentPage = currentPage,
            pageSize = totalPages,
            modifier = Modifier.align(Alignment.End)
        )

        Image(
            painter = painterResource(id =R.drawable.personal_info ),
            contentDescription = "Background Image",
        )



        Spacer(Modifier.height(16.dp))
        AppText("Personal Details",
            fontSize = 10.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))


        AppTextField(
            value = firstName,
            onValueChange = { firstName = it },
            placeholder = "First Name",
            borderColor = Color.LightGray,
            focusedBorderColor = AppGreen
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            value = lastName,
            onValueChange = { lastName = it },
            placeholder = "Last Name",
            borderColor = Color.LightGray,
            focusedBorderColor = AppGreen
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppWhiteButton(
            onClick = { showDatePicker = true },
            text = dateOfBirth.ifBlank { "Select Date of Birth" }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            AppWhiteButton(
                onClick = { showGenderDropdown = true },
                text = "Gender: $gender"
            )

            DropdownMenu(
                expanded = showGenderDropdown,
                onDismissRequest = { showGenderDropdown = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                genderOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            gender = option
                            showGenderDropdown = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        AppButton(

            onClick = {
                Log.d("profile", "sending")
                val profile = ProfileRequest(
                    firstName = firstName,
                    lastName = lastName,
                    dob = dateOfBirth,
                    gender = gender.lowercase()
                )

                Log.d("profile", profile.toString())

                if (userId == null) {
                    Log.e("profile", "Cannot submit profile because userId is null")
                } else {
                    viewModel.submitProfile(userId, profile)
                }

            },
            text = "Submit"
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedDate ->
                            dateOfBirth = dateFormatter.format(Date(selectedDate))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
