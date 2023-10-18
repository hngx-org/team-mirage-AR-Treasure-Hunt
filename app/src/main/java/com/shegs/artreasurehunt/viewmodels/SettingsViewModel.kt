package com.shegs.artreasurehunt.viewmodels

import androidx.lifecycle.ViewModel
import com.shegs.artreasurehunt.ui.states.SoundState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(): ViewModel() {

    private val _soundSettings = MutableStateFlow(SoundState())
    val soundSettings = _soundSettings.asStateFlow()

    fun updateSoundSettings(soundState: SoundState) {
        _soundSettings.update { soundState }
        if (!soundState.isSoundOn) {
            _soundSettings.update { it.copy(soundLevel = 0.0f) }
        }
    }

}