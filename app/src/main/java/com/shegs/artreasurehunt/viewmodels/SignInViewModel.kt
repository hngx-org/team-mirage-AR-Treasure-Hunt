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
class SignInViewModel @Inject constructor(
    private val repository: NetworkRepository,
) : ViewModel() {

    val hasUser = repository.firebaseAuth.currentUser?.email
    private val _onboardingUIState = MutableStateFlow(OnboardingUIState())
    val onboardingUIState = _onboardingUIState.asStateFlow()

    fun updateOnBoardingState(state: OnboardingUIState) {
        _onboardingUIState.update { state }
        _onboardingUIState.update {
            it.copy(
                user = state.user,
                isButtonEnabled = validateLoginForm()
            )
        }
        Log.i("SignInVM", "${_onboardingUIState.value}")
    }

    private fun validateLoginForm(): Boolean {
        return _onboardingUIState.value.user.email.isNotBlank() && _onboardingUIState.value.user.password.isNotBlank()
    }

    fun login() {
        viewModelScope.launch {
            try {
                if (!validateLoginForm()) {
                    throw IllegalArgumentException("email and password cannot be empty")
                }
                _onboardingUIState.update { it.copy(isLoading = true, onBoardingError = null) }
                repository.login(
                    user = _onboardingUIState.value.user,
                    onComplete = { success ->
                        _onboardingUIState.update { it.copy(isOnBoardingSuccess = success) }
                        Log.i("SignInVM","$success")
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

    fun resetSignIn() {
        _onboardingUIState.update { OnboardingUIState() }
    }
}