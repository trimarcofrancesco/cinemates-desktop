package it.unina.cinemates_desktop.viewmodel

import it.unina.cinemates_desktop.api.ResponseWrapper
import it.unina.cinemates_desktop.api.safeApiCall
import it.unina.cinemates_desktop.di.NetworkModule
import it.unina.cinemates_desktop.model.*
import it.unina.cinemates_desktop.util.AppPreferences
import it.unina.cinemates_desktop.view.LoginView
import kotlinx.coroutines.Dispatchers
import tornadofx.*

class LoginViewModel : ViewModel() {

    private val user : UserModel by inject()

    class NetworkErrorEvent(val message : String) : FXEvent()
    class GenericErrorEvent(val genericError : ErrorResponse?) : FXEvent()
    class LoginSucceededEvent(val loginResponse : LoginResponse?) : FXEvent()

    suspend fun login(username: String, password: String) {
        val credentials = UserCredential(username, password, isAdmin = "true")
        when (val response = safeApiCall(Dispatchers.IO) { NetworkModule.buildService().login(credentials) }){
            is ResponseWrapper.NetworkError -> {
                fire(NetworkErrorEvent("Connessione ad internet assente"))
            }
            is ResponseWrapper.GenericError -> {
                fire(GenericErrorEvent(response.errorData))
                println("GenericError")
            }
            is ResponseWrapper.Success -> {
                response.data.result?.let { loginResponse ->
                    user.item = loginResponse.toUser()

                    AppPreferences.loginResponse = loginResponse
                    /*AppPreferences.userPoolTokens = UserTokens(
                        accessToken = loginResponse.accessToken,
                        expiresIn = loginResponse.expiresIn,
                        refreshToken = loginResponse.refreshToken,
                        idToken = loginResponse.idToken
                    )*/

                    fire(LoginSucceededEvent(response.data.result))
                }
                println("Success")
            }
        }
    }

    fun logout() {
        AppPreferences.remove(AppPreferences.LOGIN_RESPONSE.first)

        user.item = null
        primaryStage.uiComponent<UIComponent>()?.replaceWith(LoginView::class, transition = ViewTransition.FadeThrough(.5.seconds), centerOnScreen = true)
    }
}