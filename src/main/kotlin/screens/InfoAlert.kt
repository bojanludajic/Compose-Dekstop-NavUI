package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Suppress("FunctionName")
@Composable
fun InfoAlert(
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Info",
                    modifier = Modifier
                        .align(Alignment.Center),
                    fontSize = 23.sp
                )
                IconButton(
                    onClick = { onClose() },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close info"
                    )
                }
            }
        }
        Text(
            text = "- Start placing nodes by clicking on the \"+\" icon, each square represents a " +
                    "screen in your navigation graph.",
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(start = 10.dp)
        )
        Text(
            text = "- To connect two screens, click on the first one, once it turns green, click " +
                    "on the screen that you want to navigate to.",
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(start = 10.dp)
        )
        Text(
            text = "- You can change screen names by clicking their names, and you can move the " +
                    "screens around by dragging the nodes with your mouse.",
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(start = 10.dp)
        )
        Text(
            text = "- By clicking on the \"Generate code\" button, you will create a function " +
                    "that contains the logic for navigating for each screen, which will then " +
                    "be copied to your clipboard.",
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(start = 10.dp)
        )
        Text(
            text = "- Each screen name represents a composable function " +
                    "which will by default contain a single method as a parameter: onNavigate: (String) -> Unit. " +
                    "Each time the onNavigate function is called, the currentScreen value will be changed, " +
                    "triggering a screen change.",
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(start = 10.dp)
        )
    }
}
