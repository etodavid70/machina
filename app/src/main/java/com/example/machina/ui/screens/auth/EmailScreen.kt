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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.collectLatest
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
import android.util.Patterns
import com.example.machina.R
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.IndicatorUi
import com.example.machina.ui.widgets.AppText
import com.example.machina.ui.widgets.AppTextField
import com.example.machina.ui.widgets.AuthErrorSnackbar
import com.example.machina.utils.getEmail
import com.example.machina.utils.saveEmail
import com.example.machina.view_model.auth_viewmodel.AuthStep
import com.example.machina.view_model.auth_viewmodel.AuthUiState
import com.example.machina.view_model.auth_viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row

@Composable
fun EmailScreen(
    navController: NavController,
    currentPage: Int = 0,
    totalPages: Int = 4,
    viewModel: AuthViewModel = koinViewModel()
) {

    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val isLoading = state is AuthUiState.Loading
    val snackbarHostState = remember { SnackbarHostState() }

    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }

    AuthErrorSnackbar(
        state = state,
        snackbarHostState = snackbarHostState,
        onMessageShown = viewModel::resetState
    )


    LaunchedEffect(Unit) {
        viewModel.state.collectLatest { state ->
            if (state is AuthUiState.Success && state.step == AuthStep.EmailSent) {
                viewModel.resetState()
                navController.navigate("verify")
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
            painter = painterResource(id =R.drawable.email ),
            contentDescription = "Background Image",
        )

        AppText("Verification code on your email",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))
        AppText("We  will send verification code on your email id",
            fontSize = 10.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))

//        TextField(
//            value = email,
//            onValueChange = { email= it },
//            label = { Text("What email id do you want to use?") },
//            modifier = Modifier.fillMaxWidth()
//        )
        AppTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
                viewModel.resetState()
            },
            placeholder = "What email id do you want to use?",
            borderColor = Color.LightGray,
            focusedBorderColor = AppGreen,
            errorText = emailError
        )

        Spacer(modifier = Modifier.height(50.dp))

        AppButton(
            onClick = {
                val trimmedEmail = email.trim()
                if (trimmedEmail.isBlank()) {
                    emailError = "Email is required."
                    return@AppButton
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
                    emailError = "Enter a valid email address."
                    return@AppButton
                }

                viewModel.sendEmail(trimmedEmail)
                Log.d("email saved", getEmail(context).toString())
                saveEmail(context, trimmedEmail)


                      },
            text = "Send Verification Code",
            isLoading = isLoading
        )
            Spacer(modifier = Modifier.height(10.dp))



            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = "Have an account? ",
                    fontSize = 14.sp
                )
                AppText(
                    text = "Login",
                    color = AppGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("login")
                        }
                )
            }
        }
    }
}
