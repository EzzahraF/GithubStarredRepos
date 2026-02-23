package com.example.githubstarredrepos

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.example.githubstarredrepos.data.local.preferences.FontSize
import com.example.githubstarredrepos.data.local.preferences.PrimaryColor
import com.example.githubstarredrepos.data.local.preferences.ThemeMode
import com.example.githubstarredrepos.data.local.preferences.PreferencesManager
import com.example.githubstarredrepos.data.local.preferences.dataStore
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    // On garde l'injection pour une utilisation ultérieure dans l'activité
    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun attachBaseContext(newBase: Context) {
        // 1. Appliquer la taille de police
        val dataStore = newBase.dataStore
        val preferences = runBlocking { dataStore.data.first() }
        val fontSizeName = preferences[stringPreferencesKey("font_size")] ?: FontSize.NORMAL.name
        val fontSize = FontSize.fromString(fontSizeName)

        val newConfig = Configuration(newBase.resources.configuration)
        newConfig.fontScale = fontSize.scale

        val wrappedContext = newBase.createConfigurationContext(newConfig)
        super.attachBaseContext(wrappedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 2. Récupérer MANUELLEMENT le PreferencesManager avant de l'utiliser
        // Car 'preferencesManager' (injecté) n'est pas encore prêt ici.
        val prefManager = getPreferencesManagerManually()

        // 3. Appliquer le thème
        applyThemeAndColor(prefManager)

        // 4. Appeler super.onCreate() (C'est ICI que Hilt injecte 'preferencesManager')
        super.onCreate(savedInstanceState)
    }

    private fun applyThemeAndColor(prefManager: PreferencesManager) {
        // On utilise runBlocking car on ne peut pas faire d'async avant super.onCreate()
        val themeMode = runBlocking { prefManager.themeMode.first() }
        val primaryColor = runBlocking { prefManager.primaryColor.first() }

        // Appliquer le mode Dark/Light
        val nightMode = when (themeMode) {
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)

        // Appliquer la couleur primaire
        val themeResId = when (primaryColor) {
            PrimaryColor.BLUE -> R.style.AppTheme
            PrimaryColor.GREEN -> R.style.AppTheme_Green
            PrimaryColor.PURPLE -> R.style.AppTheme_Purple
            PrimaryColor.ORANGE -> R.style.AppTheme_Orange
            PrimaryColor.RED -> R.style.AppTheme_Red
            PrimaryColor.TEAL -> R.style.AppTheme_Teal
        }

        setTheme(themeResId)
    }

    // Fonction utilitaire pour récupérer le singleton manuellement via Hilt
    private fun getPreferencesManagerManually(): PreferencesManager {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            PreferencesManagerEntryPoint::class.java
        )
        return hiltEntryPoint.preferencesManager()
    }

    // Interface "Point d'entrée" pour dire à Hilt de nous donner le PreferencesManager
    @dagger.hilt.EntryPoint
    @dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
    interface PreferencesManagerEntryPoint {
        fun preferencesManager(): PreferencesManager
    }
}
