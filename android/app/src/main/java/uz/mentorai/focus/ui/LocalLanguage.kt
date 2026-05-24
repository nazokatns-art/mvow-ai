package uz.mentorai.focus.ui

import androidx.compose.runtime.staticCompositionLocalOf
import uz.mentorai.focus.data.language.Language

/**
 * Compose hierarchy bo'ylab joriy tilni uzatadi.
 * `MentorApp` root'da `CompositionLocalProvider(LocalLanguage provides ...)` orqali sozlanadi.
 */
val LocalLanguage = staticCompositionLocalOf { Language.systemDefault() }
