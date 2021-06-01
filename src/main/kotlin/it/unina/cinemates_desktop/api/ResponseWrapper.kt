package it.unina.cinemates_desktop.api

import it.unina.cinemates_desktop.model.ErrorResponse

sealed class ResponseWrapper<out T> {
    data class Success<out T>(val data: T): ResponseWrapper<T>()
    data class GenericError(val code: Int? = null, val errorData: ErrorResponse? = null): ResponseWrapper<Nothing>()
    object NetworkError: ResponseWrapper<Nothing>()
}