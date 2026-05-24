package uz.mentorai.focus.data.language

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private val Context.languageDataStore by preferencesDataStore(name = "language")

/**
 * Foydalanuvchi tanlagan tilni saqlaydi va dasturning butun bo'ylab tarqatadi.
 *
 * `current.value` synchronous o'qish uchun — DataStore'dan eventually consistent
 * tarzda yangilanadi (default: tizim locale'i).
 */
@Singleton
class LanguageRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val ds get() = context.languageDataStore
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _current = MutableStateFlow(Language.systemDefault())
    val current: StateFlow<Language> = _current.asStateFlow()

    val currentFlow: Flow<Language> = ds.data.map { prefs ->
        Language.fromCode(prefs[KEY_CODE]) ?: Language.systemDefault()
    }

    init {
        scope.launch {
            currentFlow.collect { _current.value = it }
        }
    }

    suspend fun setLanguage(language: Language) {
        ds.edit { it[KEY_CODE] = language.code }
        _current.value = language
    }

    private companion object {
        val KEY_CODE = stringPreferencesKey("code")
    }
}
