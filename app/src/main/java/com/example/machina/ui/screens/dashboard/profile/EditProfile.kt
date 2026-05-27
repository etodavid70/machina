import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import android.util.Log
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.R
import com.example.machina.data.model.onboarding_models.ProfileRequest
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.AppTextField
import com.example.machina.ui.widgets.BackButton
import com.example.machina.ui.widgets.DashboardErrorSnackbar
import com.example.machina.view_model.dashboard_viewmodel.DashboardUiState
import com.example.machina.view_model.dashboard_viewmodel.DashboardViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: DashboardViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val isLoading = state is DashboardUiState.Loading
    val snackbarHostState = remember { SnackbarHostState() }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var profileLoaded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showGenderDropdown by remember { mutableStateOf(false) }
    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var dateOfBirthError by remember { mutableStateOf<String?>(null) }
    var formError by remember { mutableStateOf<String?>(null) }
    val datePickerState = rememberDatePickerState()
    val dateFormatter = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }
    val genderOptions = listOf("Male", "Female")

    DashboardErrorSnackbar(
        state = state,
        snackbarHostState = snackbarHostState,
        onMessageShown = viewModel::resetState
    )

    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
    }

    LaunchedEffect(profile) {
        if (!profileLoaded && (profile.firstName.isNotBlank() || profile.lastName.isNotBlank() || profile.dob != null || profile.gender.isNotBlank())) {
            firstName = profile.firstName
            lastName = profile.lastName
            dateOfBirth = profile.dob.orEmpty()
            gender = profile.gender.replaceFirstChar { it.uppercase() }.ifBlank { "Male" }
            profileLoaded = true
        }
    }

    LaunchedEffect(state) {
        if (state is DashboardUiState.Success) {
            viewModel.resetState()
            navController.popBackStack()
        }
    }



    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(
                    navController = navController,
                    modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                AppText(
                    text = "Edit profile",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(18.dp))

            Image(
                painter = painterResource(id =R.drawable.avatar ),
                contentDescription = "Background Image",
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
            )



            Spacer(Modifier.height(14.dp))
            AppText("Personal Details",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))


            AppTextField(
                value = firstName,
                onValueChange = {
                    firstName = it
                    firstNameError = null
                    formError = null
                    viewModel.resetState()
                },
                placeholder = "First Name",
                borderColor = Color.LightGray,
                focusedBorderColor = AppGreen,
                errorText = firstNameError
            )



            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                    lastNameError = null
                    formError = null
                    viewModel.resetState()
                },
                placeholder = "Last Name",
                borderColor = Color.LightGray,
                focusedBorderColor = AppGreen,
                errorText = lastNameError
            )

            Spacer(modifier = Modifier.height(16.dp))


            AppWhiteButton(
                onClick = {
                    showDatePicker = true
                    dateOfBirthError = null
                    formError = null
                },
                text = dateOfBirth.ifBlank { "Select Date of Birth" }
            )
            dateOfBirthError?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }

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

            formError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            AppButton(

                onClick = {
                    val trimmedFirstName = firstName.trim()
                    val trimmedLastName = lastName.trim()

                    firstNameError = when {
                        trimmedFirstName.isBlank() -> "First name is required."
                        trimmedFirstName.length < 2 -> "First name is too short."
                        else -> null
                    }
                    lastNameError = when {
                        trimmedLastName.isBlank() -> "Last name is required."
                        trimmedLastName.length < 2 -> "Last name is too short."
                        else -> null
                    }
                    dateOfBirthError = if (dateOfBirth.isBlank()) {
                        "Date of birth is required."
                    } else {
                        null
                    }

                    if (firstNameError != null || lastNameError != null || dateOfBirthError != null) {
                        return@AppButton
                    }

                    Log.d("profile", "sending")
                    val updatedProfile = ProfileRequest(
                        firstName = trimmedFirstName,
                        lastName = trimmedLastName,
                        dob = dateOfBirth,
                        gender = gender.lowercase()
                    )

                    Log.d("profile", updatedProfile.toString())

                    formError = null
                    viewModel.editProfile(updatedProfile)

                },
                text = "Submit",
                isLoading = isLoading
            )
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedDate ->
                            dateOfBirth = dateFormatter.format(Date(selectedDate))
                            dateOfBirthError = null
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
