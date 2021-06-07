package it.unina.cinemates_desktop.viewmodel

import it.unina.cinemates_desktop.api.ResponseWrapper
import it.unina.cinemates_desktop.api.safeApiCall
import it.unina.cinemates_desktop.di.NetworkModule
import it.unina.cinemates_desktop.model.Comment
import it.unina.cinemates_desktop.model.ErrorResponse
import it.unina.cinemates_desktop.model.InappropriatesResponse
import it.unina.cinemates_desktop.model.Review
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
        /*fire(GetInappropriatesResponse(
            InappropriatesResponse(
                listOf(
                    Review(0, "luigi", "https://scontent-fco1-1.xx.fbcdn.net/v/t1.18169-9/14034747_10202105639200517_192623426590496151_n.jpg?_nc_cat=101&ccb=1-3&_nc_sid=174925&_nc_ohc=oiYC4Qw7AOEAX_SO17J&_nc_ht=scontent-fco1-1.xx&oh=912e23540591cbb5bde1750378b87069&oe=60CD34CF", "Bella!", "012345"),
                    Review(1, "peppe", "propic2", "Top!", "012345"),
                    Review(2, "capellone", "propic3", "Biutiful!", "012345"),
                    Review(3, "mariano", "propic1", "Bella!", "012345"),
                    Review(4, "denny", "propic4", "Ninoooo!", "012345"),
                    Review(5, "francesco", "propic2", "Wa!", "012345"),
                    Review(6, "capelli unti", "propic5", "Bella!", "012345"),
                    Review(7, "mr 30", "propic1", "Solo 29??", "012345")
                ),
                listOf(
                    Comment(0, "luigi", "https://scontent-fco1-1.xx.fbcdn.net/v/t1.18169-9/14034747_10202105639200517_192623426590496151_n.jpg?_nc_cat=101&ccb=1-3&_nc_sid=174925&_nc_ohc=oiYC4Qw7AOEAX_SO17J&_nc_ht=scontent-fco1-1.xx&oh=912e23540591cbb5bde1750378b87069&oe=60CD34CF", "Bella!", "012345"),
                    Comment(1, "peppe", "propic2", "Top!", "012345"),
                    Comment(2, "capellone", "propic3", "Biutiful!", "012345"),
                    Comment(3, "mariano", "propic1", "Bella!", "012345"),
                    Comment(4, "denny", "propic4", "Ninoooo!", "012345"),
                    Comment(5, "francesco", "propic2", "Wa!", "012345"),
                    Comment(6, "capelli unti", "propic5", "Bella!", "012345"),
                    Comment(7, "mr 30", "propic1", "Solo 29??", "012345")
                )
            )
        ))*/
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