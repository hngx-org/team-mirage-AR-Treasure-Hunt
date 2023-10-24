package com.shegs.artreasurehunt.ui.states


data class SignUpUIState(
    val emailLogin: String = "",
    val userName: String = "",
    val passwordLogin: String = "",
    val userNameSignUp: String = "",
    val passwordSignUp: String = "",
    val isLoading: Boolean = false,
    val signUpError: String? = null,
    val loginError: String? = null,
)