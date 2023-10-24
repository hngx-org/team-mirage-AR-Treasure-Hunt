package com.shegs.artreasurehunt.ui.game.ar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.shegs.artreasurehunt.R
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt


@Composable
fun ARCameraScreen(
    modifier: Modifier,
    isWithinGeoFence: Boolean,
) {
    val arModelNodes = remember { mutableStateOf<ArModelNode?>(null) }
    val nodes = remember { mutableListOf<ArNode>() }
    val context = LocalContext.current

    var userScore by remember { mutableStateOf(0) }


    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.coin_icon), // Use your coin image resource
                contentDescription = null,
            )
            Text(
                text = "Score: $userScore",
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(8.dp)
                    .clip(CircleShape)
            )
        }

    }

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

// Update the user's score when the 3D object is shown
                userScore += 50
            }
        }
    }
}