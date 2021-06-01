package it.unina.cinemates_desktop.api

import it.unina.cinemates_desktop.model.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ResponseWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResponseWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResponseWrapper.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse: ErrorResponse = convertErrorBody(throwable)
                    print("HttpException code: $code, errorBody: $errorResponse")

                    /*if (code == 401) {
                        EventBus.getDefault().post(UnauthorizedEvent.newInstance(errorResponse.message))
                    }*/

                    ResponseWrapper.GenericError(code, errorResponse)
                }
                else -> {
                    ResponseWrapper.GenericError(null, ErrorResponse(throwable.toString(), ""))
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponse {
    return try {
        if (throwable.response() != null && throwable.response()!!.errorBody() != null) {
            val errorString = throwable.response()!!.errorBody()!!.string()

            return when (throwable.code()) {
                500 -> ErrorResponse("InternalServerError", "Internal Server Error")
                else -> {
                    Gson().fromJson(errorString, ErrorResponse::class.java)
                }
            }
        } else {
            ErrorResponse("ErrorBody", throwable.toString())
        }
    } catch (exception: Exception) {
        print(exception)
        ErrorResponse("Exception", exception.toString())
    }
}