import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.machina.R
import com.example.machina.data.model.onboarding_models.PasswordRequest
import com.example.machina.data.model.onboarding_models.ProfileRequest
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.AppPasswordField
import com.example.machina.ui.widgets.IndicatorUi
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.AuthErrorSnackbar
import com.example.machina.utils.getUserId
import com.example.machina.utils.saveSignupCompleted
import com.example.machina.view_model.auth_viewmodel.AuthStep
import com.example.machina.view_model.auth_viewmodel.AuthUiState
import com.example.machina.view_model.auth_viewmodel.AuthViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun PasswordScreen(
    navController: NavController,
    currentPage: Int = 3,
    totalPages: Int = 4,
    viewModel: AuthViewModel = koinViewModel()
) {

    val context= LocalContext.current

    val userId = remember { getUserId(context) }
    val state by viewModel.state.collectAsState()
    val isLoading = state is AuthUiState.Loading
    val snackbarHostState = remember { SnackbarHostState() }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var formError by remember { mutableStateOf<String?>(null) }

    AuthErrorSnackbar(
        state = state,
        snackbarHostState = snackbarHostState,
        onMessageShown = viewModel::resetState
    )

    LaunchedEffect(Unit) {
        viewModel.state.collectLatest { state ->
            if (state is AuthUiState.Success && state.step == AuthStep.PasswordSet) {
                saveSignupCompleted(context)
                viewModel.resetState()
                navController.navigate("login") {
                    popUpTo("email") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
            painter = painterResource(id =R.drawable.password ),
            contentDescription = "Background Image",
        )

        AppText("Set Password",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        AppPasswordField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
                confirmPasswordError = null
                formError = null
                viewModel.resetState()
            },
            placeholder = "Password",
            borderColor = AppGreen,
            errorText = passwordError
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppPasswordField(
            value = password2,
            onValueChange = {
                password2 = it
                confirmPasswordError = null
                formError = null
                viewModel.resetState()
            },
            placeholder = "Confirm Password",
            borderColor = AppGreen,
            errorText = confirmPasswordError
        )
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
                passwordError = when {
                    password.isBlank() -> "Password is required."
                    password.length < 8 -> "Password must be at least 8 characters."
                    else -> null
                }
                confirmPasswordError = when {
                    password2.isBlank() -> "Confirm your password."
                    password2 != password -> "Passwords do not match."
                    else -> null
                }

                if (passwordError != null || confirmPasswordError != null) {
                    return@AppButton
                }

                val passwordData = PasswordRequest(
                   password=password,
                    confirm_password = password2
                )


                if (userId == null) {
                    Log.e("profile", "Cannot submit profile because userId is null")
                    formError = "User session expired. Please start again."
                } else {
                    formError = null
                    viewModel.setPassword(userId, passwordData)
                }


            },
            text = "Submit",
            isLoading = isLoading
        )
        }
    }
}
