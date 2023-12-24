package utad.ud3_appideas.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore("utad.ud3_appideas")

object DataStoreManager{

    private const val userKey = "user_name"
    private const val userLoggedKey = "user_logged"
    private const val passwordUserKey = "password"

    suspend fun saveUser(context: Context, userName: String, password: String) {
        val userNameKey = stringPreferencesKey(userKey)
        val passwordKey = stringPreferencesKey(passwordUserKey)

        context.dataStore.edit { editor ->
            editor[userNameKey] = userName
            editor[passwordKey] = password
        }
    }

    suspend fun getUser(context: Context): Flow<String> {
        val userNameKey = stringPreferencesKey(userKey)
        return context.dataStore.data.map { editor ->
            editor[userNameKey] ?: "El valor de usuario es nulo"
        }
    }

    suspend fun getPassword(context: Context): Flow<String> {
        val passwordKey = stringPreferencesKey(passwordUserKey)
        return context.dataStore.data.map { editor ->
            editor[passwordKey] ?: "El valor de la contraseña es nulo"
        }
    }

    suspend fun setUserLogged(context: Context) {
        val userLogged = booleanPreferencesKey(userLoggedKey)
        context.dataStore.edit { editor ->
            editor[userLogged] = true
        }
    }

    suspend fun setUserNotLogged(context: Context) {
        val userLogged = booleanPreferencesKey(userLoggedKey)
        context.dataStore.edit { editor ->
            editor.remove(userLogged)
        }
    }

    suspend fun getIsUserLogged(context: Context): Flow<Boolean> {
        val userLogged = booleanPreferencesKey(userLoggedKey)
        return context.dataStore.data.map { editor ->
            editor[userLogged] ?: false
        }
    }

    suspend fun deleteUser(context: Context){
        context.dataStore.edit { editor->
            editor.clear()
        }
    }

}