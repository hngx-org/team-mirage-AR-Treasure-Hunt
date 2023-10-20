package com.shegs.artreasurehunt.ui.game.ar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun ARCameraScreen(
    modifier: Modifier,
    isWithinGeoFence: Boolean,
) {
    val arModelNodes = remember { mutableStateOf<ArModelNode?>(null) }
    val nodes = remember { mutableListOf<ArNode>() }
    val context = LocalContext.current


    Column(
        modifier = modifier
            .height(0.dp)
            .fillMaxWidth()
    ) {

        ARScene(
            modifier = Modifier,
            nodes = nodes,
            planeRenderer = true,
            onCreate = { arSceneView ->

                // Apply AR configuration here
                arModelNodes.value =
                    ArModelNode(arSceneView.engine, PlacementMode.BEST_AVAILABLE).apply {
                        followHitPosition = true
                        instantAnchor = false
                        onHitResult = { arModelNodes, hitResult ->

                        }
                        scale = Scale(0.1f)
                        position = Position(x = 0.0f, y = 0.0f, z = -2.0f)

                    }

                nodes.add(arModelNodes.value!!)
            },

            onSessionCreate = { session ->
                // Configure ARCore session
            },
            onFrame = { arFrame ->
                // Handle AR frame updates
            },
            onTap = { hitResult ->
                // Handle user interactions in AR
            }
        )
        //AnimatedColumn(navController)
        //NavigationDrawer()
    }

    LaunchedEffect(isWithinGeoFence) {
        // Load and set the 3D model (GLB) for the ArModelNode within a coroutine
        if (isWithinGeoFence) {
            val modelNode = arModelNodes.value
            if (modelNode != null) {
                withContext(Dispatchers.IO) {
                    modelNode.loadModelGlb(
                        context = context,
                        glbFileLocation = "file:///android_asset/treasure_chest.glb",
                        autoAnimate = true
                    )
                }
            }
        }
    }
}