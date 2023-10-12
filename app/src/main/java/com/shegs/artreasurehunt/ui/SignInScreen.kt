package com.shegs.artreasurehunt.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shegs.artreasurehunt.navigation.NestedNavItem
import com.shegs.artreasurehunt.ui.common.CustomRoundedButton
import com.shegs.artreasurehunt.ui.common.RoundedTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    navController: NavController,
//    viewModel: SignInViewModel = hiltViewModel()
) {
//    val state = viewModel.state.collectAsState().value
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()

//    LaunchedEffect(key1 = true) {
//        viewModel.eventFlow.collectLatest { event ->
//            when (event) {
//                SignInUiEvents.OnsuccessfulSignIn -> {
//                    navController.navigate(com.shegs.artreasurehunt.navigation.NestedNavItem.App.HomeScreen.route) {
//                        popUpTo(com.shegs.artreasurehunt.navigation.NestedNavItem.App.HomeScreen.route) {
//                            inclusive = false
//                        }
//                    }
//                }
//
//                is SignInUiEvents.ShowSnackBar -> {
//                    scope.launch { snackBarHostState.showSnackbar(event.message) }
//                }
//            }
//        }
//    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) { innerPadding ->
        SignInScreenContent(
//            state = state,
//            onEvent = viewModel::onEvent,
            navController = navController,
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding()
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreenContent(
//    state: SignInState,
//    onEvent: (SignInEvents) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Welcome back !",
                    fontWeight = FontWeight(500),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.scrim
                )
                Text(
                    text = "Sign in to access your account",
                    fontWeight = FontWeight(400),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.scrim.copy(0.6f)
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Email Address",
                    fontWeight = FontWeight(400),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.scrim.copy(0.6f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                RoundedTextField(
                    value = "",
                    label = "Email",
                    icon = Icons.Outlined.Email,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {

                    }
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
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Password",
                    fontWeight = FontWeight(400),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.scrim.copy(0.6f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                TextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = "Password",
                            fontWeight = FontWeight(400),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.scrim.copy(0.6f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "password"
                        )
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
                            focusManager.clearFocus()
                        }
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = MaterialTheme.shapes.medium,
                    maxLines = 1,
                    singleLine = true,
                )
            }
        }

        item {
            CustomRoundedButton(
                label = "Sign In",
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                filled = true,
                onClick = {
                    //onEvent(SignInEvents.OnSignIn)
                    navController.navigate(NestedNavItem.ARCameraScreen.route)
                }
            )
        }

//        item {
//            if (state.loading) {
//                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                    CircularProgressIndicator()
//                }
//            }
//        }

    }
}

