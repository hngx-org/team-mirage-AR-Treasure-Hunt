package com.shegs.artreasurehunt.ui.states


data class SignUpUIState(
    val email: String = "",
    val password: String = "",
    val userName: String = "",
    val loading: Boolean = false
)