package data.model

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset

data class GraphNode(
    val id: Int,
    var name: MutableState<String>,
    val position: Offset
)