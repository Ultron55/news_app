package example.news.data.data.remote.errors

import java.io.IOException

sealed class RequestError(
    message: String? = null,
    cause: Throwable? = null,
    val apiCode: String? = null
) : Exception(message, cause) {

    fun getClassName() = this::class.simpleName.toString()

    class ApiError(message: String, apiCode: String? = null) : RequestError(
        "API Error: $message", apiCode = apiCode
    )

    data object EmptySearchQuery : RequestError("Empty search query") {
        private fun readResolve(): Any = RateLimited
    }

    data object RateLimited : RequestError(
        "Rate limit exceeded", apiCode = "rateLimited"
    ) {
        private fun readResolve(): Any = RateLimited
    }

    data object SourcesTooMany : RequestError(
        "Too many sources specified", apiCode = "sourcesTooMany"
    ) {
        private fun readResolve(): Any = RateLimited
    }

    data object SourceDoesNotExist : RequestError(
        "Specified source does not exist", apiCode = "sourceDoesNotExist"
    ) {
        private fun readResolve(): Any = RateLimited
    }

    class BadRequest(message: String) : RequestError("Bad Request: $message")

    data object Unauthorized : RequestError("Unauthorized access") {
        private fun readResolve(): Any = Unauthorized
    }

    data object Forbidden : RequestError("Access is forbidden") {
        private fun readResolve(): Any = Forbidden
    }

    data object NotFound : RequestError("Requested resource not found") {
        private fun readResolve(): Any = NotFound
    }

    data object InternalServerError : RequestError("Internal server error") {
        private fun readResolve(): Any = InternalServerError
    }

    class NetworkError(message: String) : RequestError("Network Error: $message")

    class UnknownError(
        message: String? = null, cause: Throwable? = null
    ) : RequestError(message, cause)

    companion object {

        fun errorHandler(cause: Throwable): RequestError = when (cause) {
            is ServerError -> when {
                cause.apiCode == RateLimited.apiCode -> RateLimited
                cause.apiCode == SourcesTooMany.apiCode -> SourcesTooMany
                cause.apiCode == SourceDoesNotExist.apiCode -> SourceDoesNotExist
                cause.code == 400 -> BadRequest(
                    cause.errorMessage ?: "Invalid request parameters"
                )
                cause.code == 401 -> Unauthorized
                cause.code == 403 -> Forbidden
                cause.code == 404 -> NotFound
                cause.code == 500 -> InternalServerError
                else -> ApiError(cause.errorMessage ?: ServerError.defaultMessage)
            }
            is IOException -> NetworkError(cause.message ?: "Network error")
            else -> UnknownError(cause = cause)
        }
    }
}