package com.shegs.artreasurehunt.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shegs.artreasurehunt.data.models.User
import com.shegs.artreasurehunt.data.repositories.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: NetworkRepository,
) : ViewModel() {

    val user = repository.firebaseAuth.currentUser
    val userid = repository.firebaseAuth.currentUser?.uid

   private val userId = MutableStateFlow<String?>(null)
    val profile = userId.map { id ->
        when(id) {
            null -> User()
            else -> repository.getSignUpDetails(
                userId = id
            ) ?:User()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        initialValue = User()
    )

    fun getUserProfile(userId: String) {
        this.userId.update { userId }
    }


    fun signOut() {
        repository.signOut()
    }

    fun deleteAccount(context:Context) {
        viewModelScope.launch {
            repository.deleteAccount(
                onComplete = {
                    if (it) {
                        Toast.makeText(context,"Account Deleted Successfully", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(context,"Account Not Deleted", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}