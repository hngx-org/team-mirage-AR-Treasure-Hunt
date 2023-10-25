package com.shegs.artreasurehunt.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shegs.artreasurehunt.ui.common.CustomRoundedButton
import com.shegs.artreasurehunt.ui.common.RoundedTextField
import com.shegs.artreasurehunt.ui.states.OnboardingUIState


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    signUpState: OnboardingUIState,
    updateState: (OnboardingUIState) -> Unit,
    signUpClicked: () -> Unit,
    navigateToSignIn:() -> Unit,
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        content = { scaffoldPadding ->
            SignUpScreenContent(
                modifier = modifier.padding(scaffoldPadding),
                snackBarHostState = snackBarHostState,
                signUpState = signUpState,
                updateState = updateState,
                signUpClicked = signUpClicked,
                navigateToSignIn = navigateToSignIn,
            )
        }
    )


}

@Composable
private fun SignUpScreenContent(
    modifier: Modifier,
    snackBarHostState: SnackbarHostState,
    signUpState: OnboardingUIState,
    updateState: (OnboardingUIState) -> Unit,
    signUpClicked: () -> Unit,
    navigateToSignIn:() -> Unit,
) {

    val hasError = signUpState.onBoardingError != null

    val keyboard = LocalSoftwareKeyboardController.current

    val onSignUpClicked = remember {
        {
            keyboard?.hide()
            signUpClicked()
        }
    }

    LazyColumn(
        modifier = modifier
            .padding(12.dp)
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        item {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Welcome on board !",
                    fontWeight = FontWeight(500),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "Sign up to create an account",
                    fontWeight = FontWeight(400),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                if (signUpState.onBoardingError !=null)
                    Text(
                        text = signUpState.onBoardingError,
                        color = MaterialTheme.colorScheme.error
                    )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {

                Text(
                    text = "User Name",
                    fontWeight = FontWeight(400),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                RoundedTextField(
                    value = signUpState.user.userName,
                    label = "User Name",
                    icon = Icons.Outlined.Person,
                    onValueChange = {
                        updateState(signUpState.copy(user = signUpState.user.copy(userName = it)))
                    },
                    hasError = hasError
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Email Address",
                    fontWeight = FontWeight(400),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                RoundedTextField(
                    value = signUpState.user.email,
                    label = "Email",
                    icon = Icons.Outlined.Email,
                    onValueChange = {
                        updateState(signUpState.copy(user = signUpState.user.copy(email = it)))
                    },
                    hasError = hasError
                )
            }
        }

        item {
            var passwordVisibility by remember {
                mutableStateOf(false)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 40.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Password",
                    fontWeight = FontWeight(400),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                TextField(
                    value = signUpState.user.password,
                    onValueChange = {
                        updateState(signUpState.copy(user = signUpState.user.copy(password = it)))
                    },
                    label = {
                        Text(
                            text = "Password",
                            fontWeight = FontWeight(400),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        val icon =
                            if (passwordVisibility) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff

                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboard?.hide()
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    shape = MaterialTheme.shapes.medium,
                    maxLines = 1,
                    singleLine = true,
                )
            }
        }

        item {
            CustomRoundedButton(
                label = "Sign Up",
                enabled = signUpState.isButtonEnabled,
                filled = true,
                onClick = onSignUpClicked
            )
        }

        item {
            Spacer(modifier = modifier.height(10.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Already have an account?",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight(200),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    )
                    Spacer(modifier = modifier.width(2.dp))
                    TextButton(
                        onClick = navigateToSignIn,
                        content = {
                            Text(
                                text = "Sign In",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight(500),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            )
                        }
                    )
                }
            }
        }

        item {
            if (signUpState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (signUpState.isOnBoardingSuccess) {
                LaunchedEffect(snackBarHostState) {
                    snackBarHostState.showSnackbar("Sign Up Successful")
                }
                navigateToSignIn()
            }
        }
    }
}