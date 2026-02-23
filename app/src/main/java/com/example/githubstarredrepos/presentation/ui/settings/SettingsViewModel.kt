package com.example.githubstarredrepos.presentation.ui.settings


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubstarredrepos.data.local.preferences.FontFamily
import com.example.githubstarredrepos.data.local.preferences.FontSize
import com.example.githubstarredrepos.data.local.preferences.PreferencesManager
import com.example.githubstarredrepos.data.local.preferences.PrimaryColor
import com.example.githubstarredrepos.data.local.preferences.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    // ─── FLOWS OBSERVABLES ────────────────────────────────────

    val themeMode: StateFlow<ThemeMode> = preferencesManager.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeMode.SYSTEM
        )

    val primaryColor: StateFlow<PrimaryColor> = preferencesManager.primaryColor
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PrimaryColor.BLUE
        )

    val fontFamily: StateFlow<FontFamily> = preferencesManager.fontFamily
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FontFamily.DEFAULT
        )

    val fontSize: StateFlow<FontSize> = preferencesManager.fontSize
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FontSize.NORMAL
        )

    // ─── MÉTHODES DE SAUVEGARDE ───────────────────────────────

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            preferencesManager.setThemeMode(themeMode)
        }
    }

    fun setPrimaryColor(color: PrimaryColor) {
        viewModelScope.launch {
            preferencesManager.setPrimaryColor(color)
        }
    }

    fun setFontFamily(fontFamily: FontFamily) {
        viewModelScope.launch {
            preferencesManager.setFontFamily(fontFamily)
        }
    }

    fun setFontSize(fontSize: FontSize) {
        viewModelScope.launch {
            preferencesManager.setFontSize(fontSize)
        }
    }

    fun resetToDefaults() {
        viewModelScope.launch {
            preferencesManager.resetToDefaults()
        }
    }
}