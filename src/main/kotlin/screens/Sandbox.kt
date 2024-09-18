package screens

import DarkGreen100
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
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
    var nodes by remember { mutableStateOf(emptyList<GraphNode>()) }
    var curId by remember{ mutableStateOf(0) }
    var isPlacingNode by remember { mutableStateOf(false) }
    var selectedNode1 by remember { mutableStateOf<GraphNode?>(null) }
    var selectedNode2 by remember { mutableStateOf<GraphNode?>(null) }
    var edges by remember { mutableStateOf(emptySet<Pair<GraphNode, GraphNode>>()) }

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
                            isPlacingNode = !isPlacingNode
                            selectedNode1 = null
                            selectedNode2 = null
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Toggle node placement",
                        )
                    }
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Information",
                        )
                    }
                    IconButton(
                        onClick = {
                            nodes = emptyList()
                            edges = emptySet()
                            selectedNode1 = null
                            selectedNode2 = null
                            curId = 0
                        }
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
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(if(isPlacingNode) DarkGreen100 else Color.Gray)
                    .pointerInput(Unit) {
                        detectTapGestures {offset ->
                            val nodeX = offset.x - 200
                            val nodeY = offset.y - 100
                            val newOffset = Offset(nodeX, nodeY)
                            if(isPlacingNode) {
                                nodes = nodes + GraphNode(curId++, "Screen${curId.toString()}", newOffset)
                                isPlacingNode = false
                            }
                        }
                    }
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                edges.forEach { (node1, node2) ->
                    drawLine(
                        color = Color.Black,
                        start = node1.position + Offset(200f, 100f),
                        end = node2.position + Offset(200f, 100f),
                        strokeWidth = 4f
                    )
                }
            }

            nodes.forEach {node ->
                DrawNode(
                    node,
                    selectedNode1,
                    selectedNode2,
                    onClick = {
                        if(selectedNode1 == null) {
                            selectedNode1 = node
                        } else if(selectedNode1 != null && selectedNode2 == null) {
                            selectedNode2 = node
                            if(selectedNode1!!.id != selectedNode2!!.id) {
                                edges = edges + Pair(selectedNode1!!, selectedNode2!!)
                            }
                            selectedNode1 = null
                            selectedNode2 = null
                        }
                    },
                    onDrag = { pan ->
                        val newNodes = nodes.map {
                            if (it.id == node.id) {
                                it.copy(position = it.position + pan)
                            } else {
                                it
                            }
                        }

                        val newEdges = edges.map { (node1, node2) ->
                            val newNode1 = newNodes.find { it.id == node1.id }!!
                            val newNode2 = newNodes.find { it.id == node2.id }!!
                            newNode1 to newNode2
                        }.toSet()

                        nodes = newNodes
                        edges = newEdges
                    }
                )
            }
            Column(

            ) {
                edges.forEach { (node1, node2) ->
                    Text(text = node1.name + " -> " + node2.name)
                }
            }
        }
    }
}

@Composable
fun DrawNode(
    node: GraphNode,
    seletedNode1: GraphNode?,
    selectedNode2: GraphNode?,
    onClick: () -> Unit,
    onDrag: (Offset) -> Unit
    ) {
    var bgColor = if(seletedNode1 == node || selectedNode2 == node) Color.Green else Color.White
    Box(
        modifier = Modifier
            .offset { IntOffset(node.position.x.toInt(), node.position.y.toInt() ) }
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
        if(node.id == 0) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Navigation default"
            )
        }
        BasicText(
            text = node.name,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}
