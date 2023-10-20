package com.shegs.artreasurehunt.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shegs.artreasurehunt.data.models.LeaderBoardModel
import com.shegs.artreasurehunt.data.network.request_and_response_models.Resource
import com.shegs.artreasurehunt.data.repositories.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LeaderBoardViewModel @Inject constructor(
    private val repository: NetworkRepository
) : ViewModel() {

    val user = repository.currentUser

    private val _leaderBoard = MutableStateFlow<List<LeaderBoardModel>>(emptyList())
    val leaderBoard : StateFlow<List<LeaderBoardModel>> = _leaderBoard

    private val _leaderBoardFlow = MutableStateFlow<Resource<List<LeaderBoardModel>?>>(Resource.Loading)
    val leaderBoardFlow: StateFlow<Resource<List<LeaderBoardModel>?>> = _leaderBoardFlow


    fun saveUserScore(){
        viewModelScope.launch {
            repository.saveUserScore(
                LeaderBoardModel(
                    email = user?.email!!,
                    score = 2
                )
            )
        }
    }

    fun fetchLeaderBoard(){
        viewModelScope.launch {
            _leaderBoardFlow.value = Resource.Loading
            val result = repository.fetchLeaderBoard()
            _leaderBoardFlow.value = result
            if(result is Resource.Error){
                val errorMessage = result.message
                Log.e("LeaderBoard Error",errorMessage)
            }
        }
    }



}