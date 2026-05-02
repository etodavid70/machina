import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.machina.ui.theme.AppGreen


@Composable
fun AppButton(
    text: String,
    onClick: ()-> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    selectedColor: Color = AppGreen,
    unselectedColor: Color = Color.LightGray,
    textColor: Color = Color.White
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && isEnabled && !isLoading) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = 0.65f,
            stiffness = 500f
        ),
        label = "appButtonPressScale"
    )
    val buttonEnabled = isEnabled && !isLoading

    Button(
        onClick = onClick,
        enabled = buttonEnabled,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isEnabled) selectedColor else unselectedColor,
            disabledContainerColor = if (isLoading) selectedColor else unselectedColor,
            disabledContentColor = textColor
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = textColor
                )
            } else {
                Text(
                    text = text,
                    color = textColor
                )
            }
        }
    }
}




@Composable
fun AppWhiteButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    selectedColor: Color = Color.Transparent ,
    unselectedColor: Color = Color.LightGray,
    textColor: Color = AppGreen
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && isEnabled && !isLoading) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = 0.65f,
            stiffness = 500f
        ),
        label = "appWhiteButtonPressScale"
    )
    val buttonEnabled = isEnabled && !isLoading

    Button(
        onClick = onClick,
        enabled = buttonEnabled,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
            .border(1.dp, AppGreen, shape = RoundedCornerShape(8.dp))
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
        ,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isEnabled) selectedColor else unselectedColor,
            disabledContainerColor = if (isLoading) selectedColor else unselectedColor,
            disabledContentColor = textColor
        ),
//        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = textColor
                )
            } else {
                Text(
                    text = text,
                    color = textColor
                )
            }
        }
    }
}



//AppButton(
//text = "Continue",
//onClick = { },
//modifier = Modifier
//.width(200.dp)
//)
