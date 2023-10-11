package com.shegs.artreasurehunt.ui.states

import com.shegs.artreasurehunt.network.request_and_response_models.AuthRequest


data class SignUpUIState(
    val authRequest: AuthRequest? = AuthRequest(
        email = "", password = ""
    ),
    val loading: Boolean = false
)