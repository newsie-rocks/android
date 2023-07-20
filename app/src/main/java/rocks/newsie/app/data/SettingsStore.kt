package rocks.newsie.app.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Settings(
    val isSwitchOn: Boolean = false,
)

private const val SETTINGS_STORE_NAME = "settings"

// NB: this creates the datastore inside the Context
private val Context.dataStore by preferencesDataStore(
    name = SETTINGS_STORE_NAME
)

private object PreferencesKeys {
    val IS_SWITCH_ON = booleanPreferencesKey("is_switch_on")
}

class SettingsStore(
    private val context: Context,
) {
    val settings: Flow<Settings> = context.dataStore.data
//        .catch { exception ->
//            // dataStore.data throws an IOException when an error is encountered when reading data
//            if (exception is IOException) {
//                emit(emptyPreferences())
//            } else {
//                throw exception
//            }
        .map { preferences ->
            // Get our show completed value, defaulting to false if not set:
            val isSwitchOn = preferences[PreferencesKeys.IS_SWITCH_ON] ?: false
            Settings(isSwitchOn)
        }

    suspend fun setSwitchOn(isSwitchOn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_SWITCH_ON] = isSwitchOn
        }
    }
}