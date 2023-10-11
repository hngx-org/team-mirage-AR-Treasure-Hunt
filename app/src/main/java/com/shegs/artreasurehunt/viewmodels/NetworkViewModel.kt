package com.shegs.artreasurehunt.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shegs.artreasurehunt.network.request_and_response_models.AuthRequest
import com.shegs.artreasurehunt.network.request_and_response_models.Resource
import com.shegs.artreasurehunt.repositories.NetworkRepository
import com.shegs.artreasurehunt.ui.events.SignUpEvents
import com.shegs.artreasurehunt.ui.events.SignUpUIEvents
import com.shegs.artreasurehunt.ui.states.SignUpUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val repository: NetworkRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SignUpUIState>(SignUpUIState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<SignUpUIEvents>()
    val event = _event.asSharedFlow()

    fun onEvent(event: SignUpEvents) {
        when (event) {
            is SignUpEvents.OnEmailChanged -> {
                _state.update {
                    it.copy(authRequest = AuthRequest(email = event.email))
                }

            }

            is SignUpEvents.OnPasswordChanged -> {
                _state.update {
                    it.copy(authRequest = AuthRequest(password = event.password))
                }
            }

            is SignUpEvents.OnUserNameChanged -> {
                _state.update {
                    it.copy(authRequest = AuthRequest(userName = event.userName))
                }
            }

            is SignUpEvents.OnSignUp -> {
                signUp(
                    authRequest = AuthRequest(
                        email = _state.value.authRequest?.email,
                        password = _state.value.authRequest?.password
                    )
                )
            }

            else -> {}
        }
    }

    private fun signUp(authRequest: AuthRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.login(authRequest).onEach { response ->
                when (response) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(loading = false)
                        }
                        _event.emit(
                            SignUpUIEvents.showSnackBar(
                                message = "success"
                            )
                        )
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(loading = false)
                        }
                        _event.emit(
                            SignUpUIEvents.showSnackBar(
                                message = "error"
                            )
                        )
                    }

                    is Resource.Loading -> {
                        _state.update {
                            it.copy(loading = true)
                        }
                    }
                }

            }
        }
    }

}