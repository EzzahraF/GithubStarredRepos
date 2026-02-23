package com.example.githubstarredrepos

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.githubstarredrepos.data.local.preferences.FontFamily
import com.example.githubstarredrepos.data.local.preferences.FontSize
import com.example.githubstarredrepos.data.local.preferences.PrimaryColor
import com.example.githubstarredrepos.data.local.preferences.ThemeMode
import com.example.githubstarredrepos.databinding.ActivitySettingsBinding
import com.example.githubstarredrepos.presentation.ui.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint // IMPORTANT : Nécessaire pour Hilt
class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
        observePreferences()
    }

    private fun setupToolbar() {
        binding.toolbarSettings.setNavigationOnClickListener {
            finish()
        }
    }

    // ─── 1. OBSERVER LES CHANGEMENTS (DATA -> UI) ──────────────
    private fun observePreferences() {
        // Observe le Thème
        lifecycleScope.launch {
            viewModel.themeMode.collect { mode ->
                when (mode) {
                    ThemeMode.LIGHT -> binding.radioLight.isChecked = true
                    ThemeMode.DARK -> binding.radioDark.isChecked = true
                    ThemeMode.SYSTEM -> binding.radioSystem.isChecked = true
                }
            }
        }

        // Observe la Couleur
        lifecycleScope.launch {
            viewModel.primaryColor.collect { color ->
                updateColorSelection(color)
            }
        }

        // Observe la Police
        lifecycleScope.launch {
            viewModel.fontFamily.collect { font ->
                when (font) {
                    FontFamily.DEFAULT -> binding.radioFontDefault.isChecked = true
                    FontFamily.SERIF -> binding.radioFontSerif.isChecked = true
                    FontFamily.MONOSPACE -> binding.radioFontMonospace.isChecked = true
                }
            }
        }

        // Observe la Taille de police
        lifecycleScope.launch {
            viewModel.fontSize.collect { size ->
                when (size) {
                    FontSize.SMALL -> binding.radioFontSmall.isChecked = true
                    FontSize.NORMAL -> binding.radioFontNormal.isChecked = true
                    FontSize.LARGE -> binding.radioFontLarge.isChecked = true
                    FontSize.EXTRA_LARGE -> binding.radioFontExtraLarge.isChecked = true
                }
            }
        }
        lifecycleScope.launch {
            viewModel.themeMode.collect {
                recreate()
            }
        }

        lifecycleScope.launch {
            viewModel.primaryColor.collect {
                recreate()
            }
        }
    }

    // ─── 2. ENVOYER LES ACTIONS (UI -> DATA) ───────────────────
    private fun setupListeners() {
        // Thème
        binding.radioGroupTheme.setOnCheckedChangeListener { _, checkedId ->
            val mode = when (checkedId) {
                R.id.radioLight -> ThemeMode.LIGHT
                R.id.radioDark -> ThemeMode.DARK
                else -> ThemeMode.SYSTEM
            }
            viewModel.setThemeMode(mode)
        }

        // Couleurs (Clic sur chaque vue couleur)
        binding.colorBlue.setOnClickListener { viewModel.setPrimaryColor(PrimaryColor.BLUE) }
        binding.colorGreen.setOnClickListener { viewModel.setPrimaryColor(PrimaryColor.GREEN) }
        binding.colorPurple.setOnClickListener { viewModel.setPrimaryColor(PrimaryColor.PURPLE) }
        binding.colorOrange.setOnClickListener { viewModel.setPrimaryColor(PrimaryColor.ORANGE) }
        binding.colorRed.setOnClickListener { viewModel.setPrimaryColor(PrimaryColor.RED) }
        binding.colorTeal.setOnClickListener { viewModel.setPrimaryColor(PrimaryColor.TEAL) }

        // Police
        binding.radioGroupFont.setOnCheckedChangeListener { _, checkedId ->
            val font = when (checkedId) {
                R.id.radioFontSerif -> FontFamily.SERIF
                R.id.radioFontMonospace -> FontFamily.MONOSPACE
                else -> FontFamily.DEFAULT
            }
            viewModel.setFontFamily(font)
        }

        // Taille police
        binding.radioGroupFontSize.setOnCheckedChangeListener { _, checkedId ->
            val size = when (checkedId) {
                R.id.radioFontSmall -> FontSize.SMALL
                R.id.radioFontLarge -> FontSize.LARGE
                R.id.radioFontExtraLarge -> FontSize.EXTRA_LARGE
                else -> FontSize.NORMAL
            }
            viewModel.setFontSize(size)
        }

        // Reset
        binding.buttonReset.setOnClickListener {
            viewModel.resetToDefaults()
        }
    }

    // Helper pour visualiser la couleur sélectionnée (ajoute une bordure ou change la taille)
    private fun updateColorSelection(selectedColor: PrimaryColor) {
        // Réinitialiser toutes les vues couleur
        val colorViews = listOf(
            binding.colorBlue, binding.colorGreen, binding.colorPurple,
            binding.colorOrange, binding.colorRed, binding.colorTeal
        )
        
        colorViews.forEach { view ->
            // On réduit la taille pour déselectionner
            val params = view.layoutParams
            params.width = dpToPx(48)
            params.height = dpToPx(48)
            view.layoutParams = params
        }

        // Mettre en valeur la couleur sélectionnée
        val selectedView = when (selectedColor) {
            PrimaryColor.BLUE -> binding.colorBlue
            PrimaryColor.GREEN -> binding.colorGreen
            PrimaryColor.PURPLE -> binding.colorPurple
            PrimaryColor.ORANGE -> binding.colorOrange
            PrimaryColor.RED -> binding.colorRed
            PrimaryColor.TEAL -> binding.colorTeal
        }

        // On agrandit un peu la vue sélectionnée
        val selectedParams = selectedView.layoutParams
        selectedParams.width = dpToPx(56)
        selectedParams.height = dpToPx(56)
        selectedView.layoutParams = selectedParams
    }
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
