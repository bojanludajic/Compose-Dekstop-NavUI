package screens

import DarkGreen100
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import data.model.GraphNode


@Composable
fun GraphDrawing() {
    var lastClickPosition by remember { mutableStateOf<Offset?>(null) }
    var nodes by remember { mutableStateOf(emptyList<GraphNode>()) }
    var curId by remember{ mutableStateOf(0) }
    var nodePlacing by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            nodePlacing = !nodePlacing
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "",
                        )
                    }
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "",
                        )
                    }
                    IconButton(
                        onClick = {
                            nodes = emptyList()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "",
                        )
                    }
                }
            }
        }
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(if(nodePlacing) DarkGreen100 else Color.Gray)
                    .pointerInput(Unit) {
                        detectTapGestures {offset ->
                            val nodeX = offset.x - 200
                            val nodeY = offset.y - 100
                            val newOffset = Offset(nodeX, nodeY)
                            if(nodePlacing) {
                                nodes = nodes + GraphNode(curId++, newOffset)
                                nodePlacing = false
                            }
                        }
                    }
        ) {
            nodes.forEach {node ->
                DrawNode(node)
            }
        }

    }
}

@Composable
fun DrawNode(node: GraphNode) {
    Box(
        modifier = Modifier
            .offset { IntOffset(node.position.x.toInt(), node.position.y.toInt() ) }
            .width(200.dp)
            .height(100.dp)
            .background(Color.White)
            .border(2.dp, Color.Black)
    ) {
        BasicText(
            text = node.id.toString(),
            modifier = Modifier
                .align(Alignment.Center)
        )
    }

}