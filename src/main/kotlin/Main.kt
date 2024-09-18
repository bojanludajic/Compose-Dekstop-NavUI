
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import screens.GraphDrawing


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "NavUI"
    ) {
        GraphDrawing()
    }
}
