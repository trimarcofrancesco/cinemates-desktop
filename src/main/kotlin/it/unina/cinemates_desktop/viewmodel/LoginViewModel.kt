package it.unina.cinemates_desktop.viewmodel

import it.unina.cinemates_desktop.api.ResponseWrapper
import it.unina.cinemates_desktop.api.safeApiCall
import it.unina.cinemates_desktop.di.NetworkModule
import it.unina.cinemates_desktop.model.ErrorResponse
import it.unina.cinemates_desktop.model.LoginResponse
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.Dispatchers
import tornadofx.FXEvent
import tornadofx.ViewModel

class LoginViewModel : ViewModel() {

    val username = bind { SimpleStringProperty() }
    val password = bind { SimpleStringProperty() }

    class NetworkErrorEvent(val message : String) : FXEvent()
    class GenericErrorEvent(val genericError : ErrorResponse?) : FXEvent()
    class LoginSucceededEvent(val loginResponse : LoginResponse?) : FXEvent()

    suspend fun login(username: String, password: String) {
        when (val response = safeApiCall(Dispatchers.IO) { NetworkModule.buildService().login(hashMapOf("username" to username, "password" to password)) }){
            is ResponseWrapper.NetworkError -> {
                fire(NetworkErrorEvent("Connessione ad internet assente"))
            }
            is ResponseWrapper.GenericError -> {
                fire(GenericErrorEvent(response.errorData))
                print("GenericError")
            }
            is ResponseWrapper.Success -> {
                fire(LoginSucceededEvent(response.data.result))
                print("Success")
            }
        }
    }
}