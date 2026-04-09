import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.machina.ui.theme.AppGreen


@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    isEnabled: Boolean = true,
    selectedColor: Color = AppGreen,
    unselectedColor: Color = Color.LightGray,
    textColor: Color = Color.White
) {

    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isEnabled) selectedColor else unselectedColor
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            color = textColor
        )
    }
}


@Composable
fun AppWhiteButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    isEnabled: Boolean = true,
    selectedColor: Color = Color.Transparent ,
    unselectedColor: Color = Color.LightGray,
    textColor: Color = AppGreen
) {

    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
            .border(1.dp, AppGreen, shape = RoundedCornerShape(8.dp))
        ,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isEnabled) selectedColor else unselectedColor
        ),
//        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            color = textColor
        )
    }
}



//AppButton(
//text = "Continue",
//onClick = { },
//modifier = Modifier
//.width(200.dp)
//)