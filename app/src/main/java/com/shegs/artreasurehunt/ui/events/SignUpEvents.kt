package com.shegs.artreasurehunt.ui.events

sealed interface SignUpEvents {
    data class OnEmailChanged(val email: String) : SignUpEvents
    data class OnPasswordChanged(val password: String) : SignUpEvents
    data class OnUserNameChanged(val userName: String) : SignUpEvents
    //data class OnSignUpClicked(val authRequest: AuthRequest) : SignUpEvents
    object OnSignUp : SignUpEvents
}