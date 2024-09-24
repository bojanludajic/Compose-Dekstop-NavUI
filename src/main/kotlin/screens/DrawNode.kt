package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import data.model.GraphNode

@Composable
fun DrawNode(
    node: GraphNode,
    seletedNode1: GraphNode?,
    selectedNode2: GraphNode?,
    onClick: () -> Unit,
    onDrag: (Offset) -> Unit,
    onNameChange: (MutableState<String>) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var bgColor = if (seletedNode1 == node) Color.Green else Color.White

    Box(
        modifier = Modifier
            .offset { IntOffset(node.position.x.toInt(), node.position.y.toInt()) }
            .width(200.dp)
            .height(100.dp)
            .background(bgColor)
            .border(2.dp, Color.Black)
            .clickable(onClick = onClick)
            .pointerInput(Unit) {
                detectDragGestures { _, pan ->
                    onDrag(pan)
                }
            }
    ) {
        if (node.id == 0) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Navigation default"
            )
        }
        BasicTextField(
            value = node.name.value,
            onValueChange = { newValue ->
                onNameChange(mutableStateOf(newValue))
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .align(Alignment.Center)
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Enter) {
                        focusManager.clearFocus()
                        true
                    } else {
                        false
                    }
                },
            maxLines = 1,
            textStyle = TextStyle(
                textAlign = TextAlign.Center
            )
        )
    }
}