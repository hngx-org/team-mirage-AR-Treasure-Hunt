package com.shegs.artreasurehunt.ui.states

import com.shegs.artreasurehunt.data.models.User


data class OnboardingUIState(
    val user: User = User(),
    val isLoading: Boolean = false,
    val onBoardingError: String? = null,
    val isOnBoardingSuccess: Boolean = false,
    val isButtonEnabled: Boolean = false,
)