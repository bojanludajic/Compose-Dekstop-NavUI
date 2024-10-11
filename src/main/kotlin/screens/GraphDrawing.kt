package screens

import DarkGreen200
import GraphScreenModel
import NotificationGreen
import NotificationRed
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun GraphDrawing(screenModel: GraphScreenModel = remember { GraphScreenModel() }) {

    val clipboardManager = LocalClipboardManager.current
    var showInfo by remember { mutableStateOf(false) }
    var showCopyNotification  by remember {mutableStateOf(false) }
    var validCopySnapshot by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
            ) {
                Button(
                    onClick = {
                        if(screenModel.nodes.isNotEmpty()) {
                            screenModel.copyToClipboard(clipboardManager)
                        }
                        validCopySnapshot = screenModel.nodes.isNotEmpty()
                        showCopyNotification = true
                    }
                ) {
                    Text("Generate code")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = { screenModel.toggleNodePlacement() }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Toggle node placement",
                        )
                    }
                    IconButton(
                        onClick = {
                            showInfo = !showInfo
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Information",
                        )
                    }
                    IconButton(
                        onClick = { screenModel.clearGraph() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Clear graph",
                        )
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (screenModel.isPlacingNode) DarkGreen200 else Color.White)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        screenModel.addNode(offset)
                    }
                }
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                screenModel.edges.forEach { (node1, node2) ->
                    drawLine(
                        color = Color.Black,
                        start = node1.position + Offset(200f, 100f),
                        end = node2.position + Offset(200f, 100f),
                        strokeWidth = 4f
                    )
                }
            }

            screenModel.nodes.forEach { node ->
                DrawNode(
                    node = node,
                    seletedNode1 = screenModel.selectedNode1,
                    onClick = {
                        screenModel.onNodeClick(node)
                              },
                    onDrag = { pan ->
                        screenModel.onNodeDrag(node, pan)
                             },
                    onNameChange = { newName ->
                        screenModel.updateNodeName(node.id, newName)
                        screenModel.clearSelection()
                    }
                )
            }

            Column {
                screenModel.edges.forEach { (node1, node2) ->
                    TextButton(
                        onClick = {
                            screenModel.deleteEdge(node1, node2)
                        }
                    ) {
                        Text(
                            text = "${node1.name.value} -> ${node2.name.value}",
                            color = Color.Black
                        )
                    }
                }
            }

            if (screenModel.curId == 0) {
                Text(
                    text = if (!screenModel.isPlacingNode)
                        "Click on the + to start placing nodes"
                    else
                        "Click anywhere on the green area to place a node",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            if(showInfo) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(0.8f)
                        .align(Alignment.Center)
                        .background(Color.White)
                        .border(1.dp, Color.Black)
                ) {
                    InfoAlert {
                        showInfo = false
                    }
                }
            }
            if(showCopyNotification) {
                copyNotification(validCopySnapshot)
                LaunchedEffect(Unit) {
                    delay(3000)
                    showCopyNotification = false
                }

            }
        }
    }
}

@Composable
fun copyNotification(
    validCopy: Boolean,
) {
    var offsetY by remember { mutableStateOf(-200.dp) }
    val animatedOffsetY by animateDpAsState(
        targetValue = offsetY,
        animationSpec = tween(durationMillis = 300)
    )

    LaunchedEffect(Unit) {
        offsetY = 0.dp
    }

    val text = if(validCopy) "Navigation graph code copied to clipboard" else "Navigation graph is empty"
    val boxColor = if(validCopy) NotificationGreen else NotificationRed

    Spacer(modifier = Modifier
        .height(200.dp))

    Box(
        modifier = Modifier
            .offset(y = animatedOffsetY)
            .fillMaxWidth()
            .fillMaxHeight(0.075f)
            .background(boxColor)
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}


