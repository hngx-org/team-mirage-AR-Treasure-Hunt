package com.shegs.artreasurehunt.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.data.models.ArenaModel
import com.shegs.artreasurehunt.data.network.request_and_response_models.NetworkResult
import com.shegs.artreasurehunt.data.repositories.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class ArenaViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
): ViewModel(){

    val hasUser = networkRepository.firebaseAuth.currentUser
    private val _arenas = MutableStateFlow<List<ArenaModel>>(emptyList())
    val arenas: StateFlow<List<ArenaModel>> = _arenas

    private val _arenasFlow = MutableStateFlow<NetworkResult<List<ArenaModel>>>(NetworkResult.Loading())
    val arenasFlow: StateFlow<NetworkResult<List<ArenaModel>>> = _arenasFlow

    //Here is a list of image resource for the random image selection when and arena is created
    val arenaImages = listOf(
        R.drawable.arena_one,
        R.drawable.arena_two,
        R.drawable.arena_three,
        R.drawable.arena_four,
        R.drawable.arena_five
    )


    fun createArena(arena: ArenaModel){
        viewModelScope.launch {
            val currentArenas = _arenas.value.toMutableList()
            val randomImageResId = arenaImages.random()

            currentArenas.add(arena.copy(imageResId = randomImageResId))
            _arenas.emit(currentArenas)

            //Save the arena to Firebase
            saveArenaToFirestore(arena.copy(imageResId = randomImageResId))
        }
    }


    private fun saveArenaToFirestore(arena: ArenaModel) {
        if (hasUser!= null) {
        viewModelScope.launch {
             networkRepository.saveArena(arena)

            //if (result is NetworkResult.Success) {
                // Arena saved successfully in Firestore
            //} else if (result is NetworkResult.Error) {
                // Handle the error (e.g., show an error message)
             //   val errorMessage = result.message ?: "Failed to save arena"
                // Implement error handling as needed
             //   Log.e("ArenaViewModel", errorMessage)
            }
        }
    }

    fun fetchArenas() {
        viewModelScope.launch {
            _arenasFlow.value = NetworkResult.Loading()
             networkRepository.fetchArenas().collect { arenas ->
                 _arenasFlow.update { result ->
                     when(result) {
                         is NetworkResult.Error -> {
                             NetworkResult.Error(throwable = result.throwable)
                         }

                         is NetworkResult.Success -> {
                             NetworkResult.Success(data = result.data)
                         }

                         is NetworkResult.Loading -> {
                             NetworkResult.Loading()
                         }
                     }
                 }
                 NetworkResult.Success(data = arenas)
                _arenasFlow.value = arenas
            }
        }
    }

}