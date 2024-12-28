package example.news.data.data.remote.errors

import java.io.IOException

sealed class RequestError(
    message: String? = null, cause: Throwable? = null
) : Exception(message, cause) {
    data object EmptySearchQuery : RequestError("Search query cannot be empty") {
        private fun readResolve(): Any = EmptySearchQuery
    }
    data object InvalidDateFormat : RequestError("Invalid date format") {
        private fun readResolve(): Any = InvalidDateFormat
    }
    class ApiError(message: String) : RequestError("API Error: $message")
    class RateLimited : RequestError("Rate limit exceeded")
    class SourcesTooMany : RequestError("Too many sources specified")
    class SourceDoesNotExist : RequestError("Specified source does not exist")
    class BadRequest(message: String) : RequestError("Bad Request: $message")
    class Unauthorized : RequestError("Unauthorized access")
    class Forbidden : RequestError("Access is forbidden")
    class NotFound : RequestError("Requested resource not found")
    class InternalServerError : RequestError("Internal server error")
    class NetworkError(message: String) : RequestError("Network Error: $message")
    class UnknownError(message: String? = null, cause: Throwable? = null): RequestError(message, cause)

    companion object {
        fun errorHandler(cause: Throwable): RequestError = when (cause) {
            is ServerError -> when {
                cause.errorMessage == "rateLimited" -> RateLimited()
                cause.errorMessage == "sourcesTooMany" -> SourcesTooMany()
                cause.errorMessage == "sourceDoesNotExist" -> SourceDoesNotExist()
                cause.code == 400 -> BadRequest(
                    cause.errorMessage ?: "Invalid request parameters"
                )
                cause.code == 401 -> Unauthorized()
                cause.code == 403 -> Forbidden()
                cause.code == 404 -> NotFound()
                cause.code == 500 -> InternalServerError()
                else -> ApiError(cause.errorMessage ?: ServerError.defaultMessage)
            }
            is IOException -> NetworkError(cause.message ?: "Network error")
            else -> UnknownError(cause = cause)
        }
    }
}