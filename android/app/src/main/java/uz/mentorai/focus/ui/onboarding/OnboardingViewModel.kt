package uz.mentorai.focus.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.mentorai.focus.data.language.Language
import uz.mentorai.focus.data.language.LanguageRepository
import uz.mentorai.focus.data.onboarding.OnboardingRepository
import uz.mentorai.focus.data.onboarding.OnboardingState
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repository: OnboardingRepository,
    private val languageRepository: LanguageRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    val currentLanguage: StateFlow<Language> = languageRepository.current

    init {
        repository.state
            .onEach { _state.value = it }
            .launchIn(viewModelScope)
    }

    fun saveLanguage(language: Language) {
        viewModelScope.launch { languageRepository.setLanguage(language) }
    }

    fun saveGoal(goal: String) {
        viewModelScope.launch { repository.saveGoal(goal.trim()) }
    }

    fun saveVoicePath(path: String) {
        viewModelScope.launch { repository.saveVoicePath(path) }
    }

    fun saveBlockedPackages(packages: Set<String>) {
        viewModelScope.launch { repository.saveBlockedPackages(packages) }
    }

    fun finishOnboarding() {
        viewModelScope.launch { repository.markComplete() }
    }
}
