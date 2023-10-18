package com.shegs.artreasurehunt.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.ui.common.AnnotatedText
import com.shegs.artreasurehunt.ui.common.CustomTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataRulesScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            CustomTopBar(topTitle = stringResource(id = R.string.data_rules), onBack = onBack)
        },
        content = { contentPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        content = {
                            Column(
                                modifier = modifier
                                    .padding(12.dp)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                content = {
                                    AnnotatedText(
                                        title = "Data Collection",
                                        description = "We collect limited user data, including user profiles and location information."
                                    )
                                    Spacer(modifier = modifier.height(5.dp))

                                    AnnotatedText(
                                        title = "Purpose of Data Collection",
                                        description = "We collect this data to provide personalized and location-based experiences within our app."
                                    )

                                    Spacer(modifier = modifier.height(5.dp))
                                    AnnotatedText(
                                        title = "Data Usage",
                                        description = "Your data is used to enhance your app experience, offer personalized content, and suggest relevant features."
                                    )
                                    Spacer(modifier = modifier.height(5.dp))
                                    AnnotatedText(
                                        title = "Data Sharing",
                                        description = "We do not share your data with third parties. Your data remains within the app to improve your experience."
                                    )

                                    Spacer(modifier = modifier.height(5.dp))
                                    AnnotatedText(
                                        title = "User Consent",
                                        description = " By using our app, you implicitly consent to our data collection and usage. You can manage permissions in your device settings."
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