package screens

import DarkGreen200
import GraphScreenModel
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


@Composable
fun GraphDrawing(screenModel: GraphScreenModel = remember { GraphScreenModel() }) {

    val clipboardManager = LocalClipboardManager.current
    var showInfo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
            ) {
                Button(
                    onClick = {
                        screenModel.copyToClipboard(clipboardManager)
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
                    selectedNode2 = screenModel.selectedNode2,
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
        }

    }
}

