package com.shegs.artreasurehunt.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shegs.artreasurehunt.R
import com.shegs.artreasurehunt.data.models.ArenaModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class ArenaViewModel @Inject constructor(): ViewModel(){

    private val _arenas = MutableStateFlow<List<ArenaModel>>(emptyList())
    val arenas: StateFlow<List<ArenaModel>> = _arenas

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
        }
    }

}