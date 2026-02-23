package com.example.githubstarredrepos.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Extension pour créer le DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")
@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val dataStore = context.dataStore

    // ─── CLÉS DES PRÉFÉRENCES ────────────────────────────────
    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val PRIMARY_COLOR = stringPreferencesKey("primary_color")
        val FONT_FAMILY = stringPreferencesKey("font_family")
        val FONT_SIZE = stringPreferencesKey("font_size")
    }

    // ─── FLOWS POUR OBSERVER LES PRÉFÉRENCES ─────────────────

    val themeMode: Flow<ThemeMode> = dataStore.data.map { preferences ->
        ThemeMode.fromString(
            preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
        )
    }

    val primaryColor: Flow<PrimaryColor> = dataStore.data.map { preferences ->
        PrimaryColor.fromString(
            preferences[PreferencesKeys.PRIMARY_COLOR] ?: PrimaryColor.BLUE.name
        )
    }

    val fontFamily: Flow<FontFamily> = dataStore.data.map { preferences ->
        FontFamily.fromString(
            preferences[PreferencesKeys.FONT_FAMILY] ?: FontFamily.DEFAULT.name
        )
    }

    val fontSize: Flow<FontSize> = dataStore.data.map { preferences ->
        FontSize.fromString(
            preferences[PreferencesKeys.FONT_SIZE] ?: FontSize.NORMAL.name
        )
    }

    // ─── MÉTHODES POUR SAUVEGARDER ───────────────────────────

    suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeMode.name
        }
    }

    suspend fun setPrimaryColor(color: PrimaryColor) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PRIMARY_COLOR] = color.name
        }
    }

    suspend fun setFontFamily(fontFamily: FontFamily) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_FAMILY] = fontFamily.name
        }
    }

    suspend fun setFontSize(fontSize: FontSize) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_SIZE] = fontSize.name
        }
    }

    // ─── RESET TOUTES LES PRÉFÉRENCES ────────────────────────
    suspend fun resetToDefaults() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
