package it.unina.cinemates_desktop.viewmodel

import it.unina.cinemates_desktop.api.ResponseWrapper
import it.unina.cinemates_desktop.api.safeApiCall
import it.unina.cinemates_desktop.di.NetworkModule
import it.unina.cinemates_desktop.model.DeleteData
import it.unina.cinemates_desktop.model.ErrorResponse
import it.unina.cinemates_desktop.util.AppPreferences
import it.unina.cinemates_desktop.util.MockPreferences
import kotlinx.coroutines.Dispatchers
import tornadofx.FXEvent
import tornadofx.ViewModel

class SharedReportedItemDetailsViewModel: ViewModel() {

    class NetworkErrorEvent(val message : String) : FXEvent()
    class GenericErrorEvent(val genericError : ErrorResponse?) : FXEvent()
    class DeleteReviewOrCommentResponse(val deleteReviewOrCommentResponse : Int?) : FXEvent()
    class RestoreReviewOrCommentResponse(val restoreReviewOrCommentResponse : Int?) : FXEvent()

    suspend fun deleteReviewOrComment(data: DeleteData) {
        if (MockPreferences.isMockEnabled) {
            fire(DeleteReviewOrCommentResponse(data.itemId))
        } else {
            when (val response = safeApiCall(Dispatchers.IO) { NetworkModule.buildService().removeInappropriateItem(AppPreferences.loginResponse!!.profile.userId, AppPreferences.loginResponse!!.accessToken, data.deleteFrom, data.itemId) }){
                is ResponseWrapper.NetworkError -> {
                    fire(NetworkErrorEvent("Connessione ad internet assente"))
                }
                is ResponseWrapper.GenericError -> {
                    fire(GenericErrorEvent(response.errorData))
                    println("GenericError")
                }
                is ResponseWrapper.Success -> {
                    response.data.result?.let {
                        fire(DeleteReviewOrCommentResponse(it.itemId))
                    }
                    println("Success")
                }
            }
        }
    }

    suspend fun restoreReviewOrComment(data: DeleteData) {
        if (MockPreferences.isMockEnabled) {
            fire(RestoreReviewOrCommentResponse(data.itemId))
        } else {
            when (val response = safeApiCall(Dispatchers.IO) { NetworkModule.buildService().removeReports(AppPreferences.loginResponse!!.profile.userId, AppPreferences.loginResponse!!.accessToken, data.deleteFrom, data.itemId) }){
                is ResponseWrapper.NetworkError -> {
                    fire(NetworkErrorEvent("Connessione ad internet assente"))
                }
                is ResponseWrapper.GenericError -> {
                    fire(GenericErrorEvent(response.errorData))
                    println("GenericError")
                }
                is ResponseWrapper.Success -> {
                    response.data.result?.let {
                        fire(RestoreReviewOrCommentResponse(it.itemId))
                    }
                    println("Success")
                }
            }
        }
    }
}