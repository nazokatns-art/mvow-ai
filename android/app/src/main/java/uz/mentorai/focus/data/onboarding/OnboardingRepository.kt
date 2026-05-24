package uz.mentorai.focus.data.onboarding

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.onboardingDataStore by preferencesDataStore(name = "onboarding")

@Singleton
class OnboardingRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val ds get() = context.onboardingDataStore

    val state: Flow<OnboardingState> = ds.data.map { prefs ->
        OnboardingState(
            isComplete = prefs[KEY_COMPLETE] ?: false,
            statedGoal = prefs[KEY_GOAL].orEmpty(),
            voiceCommitmentPath = prefs[KEY_VOICE_PATH],
            blockedPackages = prefs[KEY_BLOCKED] ?: emptySet()
        )
    }

    suspend fun saveGoal(goal: String) = ds.edit { it[KEY_GOAL] = goal }

    suspend fun saveVoicePath(path: String) = ds.edit { it[KEY_VOICE_PATH] = path }

    suspend fun saveBlockedPackages(packages: Set<String>) =
        ds.edit { it[KEY_BLOCKED] = packages }

    suspend fun markComplete() = ds.edit { it[KEY_COMPLETE] = true }

    companion object {
        private val KEY_COMPLETE = booleanPreferencesKey("complete")
        private val KEY_GOAL = stringPreferencesKey("goal")
        private val KEY_VOICE_PATH = stringPreferencesKey("voice_path")
        private val KEY_BLOCKED = stringSetPreferencesKey("blocked_packages")
    }
}

data class OnboardingState(
    val isComplete: Boolean = false,
    val statedGoal: String = "",
    val voiceCommitmentPath: String? = null,
    val blockedPackages: Set<String> = emptySet()
)
