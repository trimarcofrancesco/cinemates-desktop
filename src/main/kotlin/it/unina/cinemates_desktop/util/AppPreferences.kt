package it.unina.cinemates_desktop.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import it.unina.cinemates_desktop.model.LoginResponse
import it.unina.cinemates_desktop.model.UserTokens
import java.util.prefs.Preferences

object AppPreferences {

    private const val NAME = "CinematesPrefs"
    private lateinit var preferences: Preferences

    // list of app specific preferences
    val USER_POOL_TOKENS = Pair("UserPoolTokens", "")
    val LOGIN_RESPONSE = Pair("LoginResponse", null)

    fun init() {
        preferences = Preferences.userRoot().node(NAME)
    }

    var loginResponse: LoginResponse?
        // custom getter to get a preference of a desired type, with a predefined default value
        get() {
            val json = preferences.get(LOGIN_RESPONSE.first, LOGIN_RESPONSE.second)
            if (!json.isNullOrBlank()) {
                return Gson().fromJson(json, LoginResponse::class.java)
            }
            return null
        }
        // custom setter to save a preference back to preferences file
        set(value) = preferences.let {
            val gson = GsonBuilder().create().toJson(value)
            it.put(LOGIN_RESPONSE.first, gson)
        }

    var userPoolTokens: UserTokens?
        // custom getter to get a preference of a desired type, with a predefined default value
        get() {
            val json = preferences.get(USER_POOL_TOKENS.first, USER_POOL_TOKENS.second)
            if (!json.isNullOrBlank()) {
                return Gson().fromJson(json, UserTokens::class.java)
            }
            return null
        }
        // custom setter to save a preference back to preferences file
        set(value) = preferences.let {
            val gson = GsonBuilder().create().toJson(value)
            it.put(USER_POOL_TOKENS.first, gson)
        }

    fun remove(key: String) = preferences.remove(key)

}