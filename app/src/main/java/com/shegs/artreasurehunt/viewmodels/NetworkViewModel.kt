package com.shegs.artreasurehunt.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shegs.artreasurehunt.data.network.request_and_response_models.AuthRequest
import com.shegs.artreasurehunt.data.network.request_and_response_models.Resource
import com.shegs.artreasurehunt.data.repositories.NetworkRepository
import com.shegs.artreasurehunt.ui.events.SignUpEvents
import com.shegs.artreasurehunt.ui.events.SignUpUIEvents
import com.shegs.artreasurehunt.ui.states.SignUpUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class NetworkViewModel @Inject constructor(
    private val repository: NetworkRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpUIState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<SignUpUIEvents>()
    val event = _event.asSharedFlow()

    fun onEvent(event: SignUpEvents) {
        when (event) {
            is SignUpEvents.OnEmailChanged -> {
                _state.update {
                    it.copy(email = event.email)
                }

            }

            is SignUpEvents.OnPasswordChanged -> {
                _state.update {
                    it.copy(password = event.password)
                }
            }

            is SignUpEvents.OnUserNameChanged -> {
                _state.update {
                    it.copy(userName = event.userName)
                }
            }

            is SignUpEvents.OnSignUp -> {
                signUp(
                    authRequest = AuthRequest(
                        email = _state.value.email,
                        password = _state.value.password,
                        userName = _state.value.userName
                    )
                )
            }

            else -> {}
        }
    }

    private fun signUp(authRequest: AuthRequest) {
        println("log hereeeeee")
        viewModelScope.launch(Dispatchers.IO) {
            repository.signUp(authRequest).onEach { response ->
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