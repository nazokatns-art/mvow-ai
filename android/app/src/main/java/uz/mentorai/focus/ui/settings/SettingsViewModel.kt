package uz.mentorai.focus.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.mentorai.focus.data.language.Language
import uz.mentorai.focus.data.language.LanguageRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val languageRepository: LanguageRepository
) : ViewModel() {

    val currentLanguage: StateFlow<Language> = languageRepository.current

    fun setLanguage(language: Language) {
        viewModelScope.launch { languageRepository.setLanguage(language) }
    }
}
