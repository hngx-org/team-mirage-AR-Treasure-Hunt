package com.shegs.artreasurehunt.viewmodels

import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.shegs.artreasurehunt.data.network.request_and_response_models.AuthRequest
import com.shegs.artreasurehunt.data.network.request_and_response_models.Resource
import com.shegs.artreasurehunt.data.repositories.LocationRepository
import com.shegs.artreasurehunt.data.repositories.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NetworkViewModel @Inject constructor(
    private val repository: NetworkRepository,
    private val locationRepository: LocationRepository,
) : ViewModel() {

    var currentLocation by mutableStateOf<Location?>(null)
    private val _signUpFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signUpFlow: StateFlow<Resource<FirebaseUser>?> = _signUpFlow
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    fun getCurrentLocation() {
        viewModelScope.launch {
            currentLocation = locationRepository.getCurrentLocation()
            Log.d("current user location", currentLocation?.longitude.toString())
        }
    }


    fun signUp(authRequest: AuthRequest) {
        viewModelScope.launch {
            _signUpFlow.value = Resource.Loading
            val result = repository.signUp(authRequest)
            _signUpFlow.value = result
        }

    }

    fun login(authRequest: AuthRequest) {
        viewModelScope.launch {
            _loginFlow.value = Resource.Loading
            val result = repository.login(authRequest)
            _loginFlow.value = result
            getCurrentLocation()
        }

    }
}