import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.machina.R
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.IndicatorUi
import com.example.machina.ui.widgets.AppText
import com.example.machina.view_model.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun VerificationScreen(
    navController: NavController,
    currentPage: Int = 1,
    totalPages: Int = 4,
    viewModel: AuthViewModel = koinViewModel()
) {

    var code by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.Start,
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
//
        )

        AppText("Email Verification",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))
        AppText("Please enter 6 digit code sent to your email",
            fontSize = 10.sp,
            fontWeight = FontWeight.Light
        )
        Spacer(Modifier.height(16.dp))

        TextField(
            value = code,
            onValueChange = { code= it },
            label = { Text("Resend Code ", color = AppGreen) },
            modifier = Modifier.fillMaxWidth()
        )



        Spacer(modifier = Modifier.height(50.dp))

        AppButton(
            onClick = {
                viewModel.sendEmail(code)

                      },
            text = "Validate Code"
        )
    }
}