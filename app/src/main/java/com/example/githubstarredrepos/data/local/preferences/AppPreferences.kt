package com.example.githubstarredrepos.data.local.preferences
// ─── ENUM : THÈME ────────────────────────────────────────
enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM;  // Suit le thème du système

    companion object {
        fun fromString(value: String): ThemeMode {
            return try {
                valueOf(value)
            } catch (e: Exception) {
                SYSTEM
            }
        }
    }
}

// ─── ENUM : COULEUR PRIMAIRE ─────────────────────────────
enum class PrimaryColor(val colorHex: String) {
    BLUE("#1976D2"),       // Bleu par défaut (GitHub)
    GREEN("#4CAF50"),      // Vert
    PURPLE("#9C27B0"),     // Violet
    ORANGE("#FF9800"),     // Orange
    RED("#F44336"),        // Rouge
    TEAL("#009688");       // Bleu-vert

    companion object {
        fun fromString(value: String): PrimaryColor {
            return try {
                valueOf(value)
            } catch (e: Exception) {
                BLUE
            }
        }
    }
}

// ─── ENUM : POLICE ────────────────────────────────────────
enum class FontFamily {
    DEFAULT,    // Roboto (système)
    SERIF,      // Serif
    MONOSPACE;  // Monospace

    companion object {
        fun fromString(value: String): FontFamily {
            return try {
                valueOf(value)
            } catch (e: Exception) {
                DEFAULT
            }
        }
    }
}

// ─── ENUM : TAILLE DE POLICE ──────────────────────────────
enum class FontSize(val scale: Float) {
    SMALL(0.85f),
    NORMAL(1.0f),
    LARGE(1.15f),
    EXTRA_LARGE(1.3f);

    companion object {
        fun fromString(value: String): FontSize {
            return try {
                valueOf(value)
            } catch (e: Exception) {
                NORMAL
            }
        }
    }
}
