
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.squareup.kotlinpoet.*
import data.model.GraphNode

class GraphScreenModel {
    var nodes by mutableStateOf<List<GraphNode>>(emptyList())
        private set
    var edges by mutableStateOf<Set<Pair<GraphNode, GraphNode>>>(emptySet())
        private set
    var curId by mutableStateOf(0)
        private set
    var isPlacingNode by mutableStateOf(false)
        private set
    var selectedNode1 by mutableStateOf<GraphNode?>(null)
        private set
    var selectedNode2 by mutableStateOf<GraphNode?>(null)
        private set

    fun toggleNodePlacement() {
        isPlacingNode = !isPlacingNode
        selectedNode1 = null
        selectedNode2 = null
    }

    fun clearGraph() {
        nodes = emptyList()
        edges = emptySet()
        selectedNode1 = null
        selectedNode2 = null
        curId = 0
    }

    fun clearSelection() {
        selectedNode1 = null
    }

    fun deleteEdge(node1: GraphNode, node2: GraphNode) {
        edges = edges.filter { it.first != node1 || it.second != node2 }.toSet()
    }

    fun addNode(position: Offset) {
        val newNodePosition = Offset(position.x - 200, position.y - 100)
        if (isPlacingNode) {
            nodes = nodes + GraphNode(curId, mutableStateOf("Screen$curId"), newNodePosition)
            curId++
            isPlacingNode = false
        }
    }

    fun onNodeClick(node: GraphNode) {
        if (selectedNode1 == null) {
            selectedNode1 = node
        } else if (selectedNode1 != null && selectedNode2 == null) {
            selectedNode2 = node
            if (selectedNode1!!.id != selectedNode2!!.id) {
                edges = edges + Pair(selectedNode1!!, selectedNode2!!)
            }
            selectedNode1 = null
            selectedNode2 = null
        }
    }

    fun onNodeDrag(node: GraphNode, pan: Offset) {
        nodes = nodes.map {
            if (it.id == node.id) {
                it.copy(position = it.position + pan)
            } else {
                it
            }
        }
        
        edges = edges.map { (node1, node2) ->
            val newNode1 = nodes.find { it.id == node1.id }!!
            val newNode2 = nodes.find { it.id == node2.id }!!
            newNode1 to newNode2
        }.toSet()
    }

    fun updateNodeName(nodeId: Int, newName: MutableState<String>) {
        nodes = nodes.map {
            if (it.id == nodeId) {
                it.copy(
                    name = newName
                )
            } else  {
                it
            }
        }
    }

    fun generateNavigationGraph(): String {
        val composableAnnotation = AnnotationSpec.builder(ClassName("androidx.compose.runtime", "Composable")).build()

        val navigationGraphFunction = FunSpec.builder("NavigationGraph")
            .addAnnotation(composableAnnotation)
            .addCode(generateNavigationCode())
            .build()

        val file = FileSpec.builder("com.example.navigation", "NavigationGraph")
            .addType(generateScreenEnums())
            .addFunction(navigationGraphFunction)
            .build()

        return file.toString()
    }

    private fun generateNavigationCode(): CodeBlock {
        val codeBlock = CodeBlock.builder()
        if(nodes.isNotEmpty()) {
            codeBlock.addStatement("")
            codeBlock.addStatement("var currentScreen = remember { mutableStateOf(Screen.${nodes.first().name.value.toUpperCase()}) }\n")

            codeBlock.addStatement("when (currentScreen) {")

            for (node in nodes) {
                val adjacentVertices = edges.filter { it.first.id == node.id }
                if(adjacentVertices.isNotEmpty()) {
                    codeBlock.addStatement("    ${node.name.value.toUpperCase()} -> ${node.name.value}()")
                    if (adjacentVertices.isNotEmpty()) {
                        codeBlock.addStatement("    {")
                        codeBlock.addStatement("        currentScreen.value = it ")
                        codeBlock.addStatement("    }")
                    }
                }
            }
            codeBlock.add("}\n")
        }
        return codeBlock.build()
    }

    private fun generateScreenEnums(): TypeSpec {
        val screenEnum = TypeSpec.enumBuilder("Screen")

        nodes.forEach { node ->
            screenEnum.addEnumConstant(node.name.value.toUpperCase())
        }
        return screenEnum.build()
    }

    fun copyToClipboard(clipboardManager: ClipboardManager) {
        // hack to remove extra trailing comma after the last enum
        val navigationGraph = generateNavigationGraph().replace(",\n}", "\n}")
        clipboardManager.setText(AnnotatedString(navigationGraph))
    }
}
