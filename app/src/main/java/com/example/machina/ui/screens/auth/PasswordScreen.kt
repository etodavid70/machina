import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.R
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.AppPasswordField
import com.example.machina.ui.widgets.IndicatorUi
import com.example.machina.ui.widgets.AppText
import com.example.machina.view_model.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PasswordScreen(
    navController: NavController,
    currentPage: Int = 3,
    totalPages: Int = 4,
    viewModel: AuthViewModel = koinViewModel()
) {

    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }

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
            onValueChange = { password = it },
            placeholder = "Password",
            borderColor = AppGreen
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppPasswordField(
            value = password2,
            onValueChange = { password2 = it },
            placeholder = "Confirm Password",
            borderColor = AppGreen
        )
        Spacer(modifier = Modifier.height(50.dp))
        AppButton(
            onClick = {
                viewModel.setPassword(password, password2)
                navController.navigate("login")

            },
            text = "Submit"
        )
    }
}