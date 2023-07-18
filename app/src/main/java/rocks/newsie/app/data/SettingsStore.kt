package rocks.newsie.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Settings(
    val showCompleted: Boolean,
    val sorted: Boolean,
)

private const val SETTINGS_STORE_NAME = "settings"

// NB: this creates the datastore
private val Context.dataStore by preferencesDataStore(
    name = SETTINGS_STORE_NAME
)

private object PreferencesKeys {
    val SHOW_COMPLETED = booleanPreferencesKey("show_completed")
    val SORTED = booleanPreferencesKey("sorted")
}

class SettingsRepository(
    private val dataStore: DataStore<Preferences>,
    context: Context
) {
    val settingsFlow: Flow<Settings> = dataStore.data
//        .catch { exception ->
//            // dataStore.data throws an IOException when an error is encountered when reading data
//            if (exception is IOException) {
//                emit(emptyPreferences())
//            } else {
//                throw exception
//            }
        .map { preferences ->
            // Get our show completed value, defaulting to false if not set:
            val showCompleted = preferences[PreferencesKeys.SHOW_COMPLETED] ?: false
            val sorted = preferences[PreferencesKeys.SORTED] ?: false
            Settings(showCompleted, sorted)
        }

    suspend fun updateShowCompleted(showCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_COMPLETED] = showCompleted
        }
    }
}


//
//// implement the methods to
//class PreferenceDataStoreHelper(context: Context) : IPreferenceDataStoreAPI {
//
//    // dataSource access the DataStore file and does the manipulation based on our requirements.
//    private val dataSource = context.dataStore
//
//    /* This returns us a flow of data from DataStore.
//    Basically as soon we update the value in Datastore,
//    the values returned by it also changes. */
//    override suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T):
//            Flow<T> = dataSource.data.catch { exception ->
//        if (exception is IOException) {
//            emit(emptyPreferences())
//        } else {
//            throw exception
//        }
//    }.map { preferences ->
//        val result = preferences[key] ?: defaultValue
//        result
//    }
//
//    /* This returns the last saved value of the key. If we change the value,
//        it wont effect the values produced by this function */
//    override suspend fun <T> getFirstPreference(key: Preferences.Key<T>, defaultValue: T):
//            T = dataSource.data.first()[key] ?: defaultValue
//
//    // This Sets the value based on the value passed in value parameter.
//    override suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
//        dataSource.edit { preferences ->
//            preferences[key] = value
//        }
//    }
//
//    // This Function removes the Key Value pair from the datastore, hereby removing it completely.
//    override suspend fun <T> removePreference(key: Preferences.Key<T>) {
//        dataSource.edit { preferences ->
//            preferences.remove(key)
//        }
//    }
//
//    // This function clears the entire Preference Datastore.
//    override suspend fun <T> clearAllPreference() {
//        dataSource.edit { preferences ->
//            preferences.clear()
//        }
//    }
//}