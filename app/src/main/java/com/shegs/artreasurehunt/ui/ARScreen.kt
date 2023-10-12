package com.shegs.artreasurehunt.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.ar.sceneform.rendering.ModelRenderable
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.navigation.NavigationDrawer
import com.shegs.artreasurehunt.navigation.NestedNavItem
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


@SuppressLint("RememberReturnType", "SuspiciousIndentation")
@Composable
fun ARCameraScreen(navController: NavController) {
    val arModelNodes = remember { mutableStateOf<ArModelNode?>(null) }
    val nodes = remember { mutableListOf<ArNode>() }
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
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
        }
        AnimatedColumn(navController)
        //NavigationDrawer()
    }

    LaunchedEffect(true) {
        // Load and set the 3D model (GLB) for the ArModelNode within a coroutine
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


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedColumn(navController: NavController) {

        var isVisible by remember { mutableStateOf(false) }

        LaunchedEffect(isVisible) {
            if (!isVisible) {
                // Delay to allow initial slide-in animation
                delay(1000) // Adjust the delay time as needed
                isVisible = true
            }
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(2000, easing = LinearEasing)
            ),
            exit = ExitTransition.None
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "AR Treasure Hunt",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(8.dp),
                        fontSize = 30.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_black))
                    )

                    Button(
                        onClick = { navController.navigate(NestedNavItem.SignInScreen.route) },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Start Hunting")
                    }

                    Button(
                        onClick = { /* Handle button click */ },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("How to hunt")
                    }
                }
            }
        }

}