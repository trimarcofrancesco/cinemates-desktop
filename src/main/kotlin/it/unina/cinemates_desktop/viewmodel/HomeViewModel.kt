package it.unina.cinemates_desktop.viewmodel

import it.unina.cinemates_desktop.api.ResponseWrapper
import it.unina.cinemates_desktop.api.safeApiCall
import it.unina.cinemates_desktop.di.NetworkModule
import it.unina.cinemates_desktop.model.ErrorResponse
import it.unina.cinemates_desktop.model.InappropriatesResponse
import it.unina.cinemates_desktop.util.AppPreferences
import kotlinx.coroutines.Dispatchers
import tornadofx.FXEvent
import tornadofx.ViewModel

class HomeViewModel: ViewModel() {

    private var userId: String = AppPreferences.loginResponse!!.profile.userId
    private var authorization: String = AppPreferences.loginResponse!!.accessToken

    class NetworkErrorEvent(val message : String) : FXEvent()
    class GenericErrorEvent(val genericError : ErrorResponse?) : FXEvent()
    class GetInappropriatesResponse(val getInappropriatesResponse : InappropriatesResponse?) : FXEvent()

    suspend fun getInappropriates() {
        when (val response = safeApiCall(Dispatchers.IO) { NetworkModule.buildService().getInappropriates(userId, authorization) }){
            is ResponseWrapper.NetworkError -> {
                fire(NetworkErrorEvent("Connessione ad internet assente"))
            }
            is ResponseWrapper.GenericError -> {
                fire(GenericErrorEvent(response.errorData))
                println("GenericError")
            }
            is ResponseWrapper.Success -> {
                fire(GetInappropriatesResponse(response.data.result))
                println("Success")
            }
        }
    }
}