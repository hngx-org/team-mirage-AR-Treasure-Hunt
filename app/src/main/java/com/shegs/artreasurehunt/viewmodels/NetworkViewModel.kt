package com.shegs.artreasurehunt.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.shegs.artreasurehunt.data.models.User
import com.shegs.artreasurehunt.data.network.request_and_response_models.AuthRequest
import com.shegs.artreasurehunt.data.network.request_and_response_models.NetworkResult
import com.shegs.artreasurehunt.data.repositories.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class NetworkViewModel @Inject constructor(
    private val repository: NetworkRepository,
) : ViewModel() {


    val hasUser = repository.currentUser
    val userData = MutableStateFlow(User())

    init {
        getUserDetails()
    }
    private fun getUserDetails() {
        if (hasUser!= null) {
            userData.update {
                it.copy(
                    email = repository.currentUser?.email,
                )
            }
        }
    }

    private val _signUpFlow = MutableStateFlow<NetworkResult<FirebaseUser>?>(null)
    private val _loginFlow = MutableStateFlow<NetworkResult<FirebaseUser>?>(null)
    val signUpFlow: StateFlow<NetworkResult<FirebaseUser>?> = _signUpFlow
    val loginFlow: StateFlow<NetworkResult<FirebaseUser>?> = _loginFlow



    fun signUp(authRequest: AuthRequest) {
        viewModelScope.launch {
            _signUpFlow.value = NetworkResult.Loading()
            val result = repository.signUp(authRequest)
            _signUpFlow.value = NetworkResult.Success(data = result.user)
        }

    }

    fun login(authRequest: AuthRequest) {
        viewModelScope.launch {
            _loginFlow.value = NetworkResult.Loading()
            val result = repository.login(authRequest)
            _loginFlow.value = NetworkResult.Success(result.user)
           // getCurrentLocation()
        }

    }

    fun signOut() {
        repository.signOut()
    }
}