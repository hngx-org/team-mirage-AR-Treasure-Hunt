package com.shegs.artreasurehunt.data.models

import com.shegs.artreasurehunt.R

data class ArenaModel(
    val id: String = "",
    val arenaName: String = "",
    val arenaDesc: String = "",
    val arenaLocation: String = "",
    val imageResId: Int =  R.drawable.arena_one
)
