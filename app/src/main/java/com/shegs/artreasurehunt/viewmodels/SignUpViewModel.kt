package com.shegs.artreasurehunt.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shegs.artreasurehunt.data.repositories.NetworkRepository
import com.shegs.artreasurehunt.ui.states.OnboardingUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: NetworkRepository,
) : ViewModel() {


    private val _onboardingUIState = MutableStateFlow(OnboardingUIState())
    val onboardingUIState = _onboardingUIState.asStateFlow()

    fun updateOnBoardingState(state: OnboardingUIState) {
        _onboardingUIState.update { state }
        _onboardingUIState.update {
            it.copy(
                user = state.user,
                isButtonEnabled = validateSignUpForm(),
            )
        }
        Log.i("SignUpVM", "${_onboardingUIState.value}")
    }

    private fun validateSignUpForm(): Boolean {
        return _onboardingUIState.value.user.email.isNotBlank() && _onboardingUIState.value.user.password.isNotBlank()
                && _onboardingUIState.value.user.userName.isNotBlank()
    }

    fun signUp() {
        viewModelScope.launch {
            try {
                if (!validateSignUpForm()) {
                    throw IllegalArgumentException("email, password and username cannot be empty")
                }
                _onboardingUIState.update { it.copy(isLoading = true, onBoardingError = null) }
                Log.i("SIGNUPVM","${_onboardingUIState.value.user}")
                repository.signUp(
                    user = _onboardingUIState.value.user,
                    onComplete = { success ->
                        _onboardingUIState.update { it.copy(isOnBoardingSuccess = success) }
                    }
                )
            } catch (e: Exception) {
                _onboardingUIState.update { it.copy(onBoardingError = e.localizedMessage) }
                e.printStackTrace()
            } finally {
                _onboardingUIState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun resetSignUp() {
        _onboardingUIState.update { OnboardingUIState() }
    }
}