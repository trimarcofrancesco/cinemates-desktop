package it.unina.cinemates_desktop.viewmodel

import it.unina.cinemates_desktop.api.ResponseWrapper
import it.unina.cinemates_desktop.api.safeApiCall
import it.unina.cinemates_desktop.di.NetworkModule
import it.unina.cinemates_desktop.model.DeleteData
import it.unina.cinemates_desktop.model.ErrorResponse
import it.unina.cinemates_desktop.util.AppPreferences
import kotlinx.coroutines.Dispatchers
import tornadofx.FXEvent
import tornadofx.ViewModel

class SharedReportedItemDetailsViewModel: ViewModel() {

    private var userId: String = AppPreferences.loginResponse!!.profile.userId
    private var authorization: String = AppPreferences.loginResponse!!.accessToken

    class NetworkErrorEvent(val message : String) : FXEvent()
    class GenericErrorEvent(val genericError : ErrorResponse?) : FXEvent()
    class DeleteItemResponse(val deleteItemResponse : Int?) : FXEvent()
    class DeleteReportsResponse(val deleteReportsResponse : Int?) : FXEvent()

    suspend fun deleteItem(data: DeleteData){
        when (val response = safeApiCall(Dispatchers.IO) { NetworkModule.buildService().removeInappropriateItem(userId, authorization, data.deleteFrom, data.itemId) }){
            is ResponseWrapper.NetworkError -> {
                fire(NetworkErrorEvent("Connessione ad internet assente"))
            }
            is ResponseWrapper.GenericError -> {
                fire(GenericErrorEvent(response.errorData))
                println("GenericError")
            }
            is ResponseWrapper.Success -> {
                fire(DeleteItemResponse(response.data.result))
                println("Success")
            }
        }
    }

    suspend fun deleteReports(data: DeleteData){
        when (val response = safeApiCall(Dispatchers.IO) { NetworkModule.buildService().removeReports(userId, authorization, data.deleteFrom, data.itemId) }){
            is ResponseWrapper.NetworkError -> {
                fire(NetworkErrorEvent("Connessione ad internet assente"))
            }
            is ResponseWrapper.GenericError -> {
                fire(GenericErrorEvent(response.errorData))
                println("GenericError")
            }
            is ResponseWrapper.Success -> {
                fire(DeleteReportsResponse(response.data.result))
                println("Success")
            }
        }
    }
}