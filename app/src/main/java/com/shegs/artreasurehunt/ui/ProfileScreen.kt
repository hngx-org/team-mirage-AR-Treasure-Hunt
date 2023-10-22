package com.shegs.artreasurehunt.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.ui.common.AnnotatedText
import com.shegs.artreasurehunt.ui.common.CustomTopBar
import com.shegs.artreasurehunt.ui.common.MenuDialogButton

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    emailAddress: String,
    userName: String,
    email:String,
    onSignOutClick: () -> Unit,
    onDeleteAccount: () -> Unit,
    onBack: () -> Unit,
) {

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.pirate_eye_anim)
    )

    Scaffold(
        topBar = {
            CustomTopBar(topTitle = "Profile", onBack = onBack)
        },
        content = { contentPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(contentPadding)
                    .padding(13.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    Spacer(modifier = modifier.height(15.dp))
                    Box(
                        modifier = modifier.size(150.dp),
                        contentAlignment = Alignment.Center,
                        content = {
                            LottieAnimation(
                                modifier = modifier,
                                composition = composition,
                                iterations = LottieConstants.IterateForever,
                            )
                        }
                    )
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        content = {
                            Column(
                                modifier = modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                content = {
                                    Text(
                                        text = "Hello Hunter",
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight(700),
                                        fontFamily = FontFamily(Font(R.font.rye_regular)),
                                        textAlign = TextAlign.Center
                                    )
                                    AnnotatedText(title = "Email Address", description = emailAddress)
                                    AnnotatedText(title = "User Name", description = userName)
                                    Spacer(modifier = modifier.height(12.dp))
                                    MenuDialogButton(
                                        onButtonClick = onSignOutClick,
                                        stringId = R.string.sign_out,
                                        icon = {
                                            Image(
                                                painter = painterResource(id = R.drawable.logout),
                                                contentDescription = "",
                                                modifier = modifier.size(40.dp)
                                            )
                                        }
                                    )
                                    MenuDialogButton(
                                        onButtonClick = onDeleteAccount,
                                        stringId = R.string.delete,
                                        containerColor = MaterialTheme.colorScheme.error,
                                        contentColor = Color.White,
                                        icon = {
                                            Image(
                                                imageVector = Icons.Default.Delete,
                                                colorFilter = ColorFilter.tint(color = Color.White),
                                                contentDescription = "",
                                                modifier = modifier.size(40.dp)
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
            )
        }
    )

}