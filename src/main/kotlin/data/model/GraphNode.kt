package data.model

import androidx.compose.ui.geometry.Offset

data class GraphNode(
    val id: Int,
    val name: String,
    val position: Offset
)