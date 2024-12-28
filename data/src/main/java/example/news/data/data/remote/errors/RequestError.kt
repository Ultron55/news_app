package example.news.data.data.remote.errors

import java.io.IOException

sealed class RequestError(
    message: String? = null,
    cause: Throwable? = null,
    val apiCode: String? = null
) : Exception(message, cause) {

    fun getClassName() = this::class.simpleName.toString()

    class EmptySearchQuery : RequestError("Empty search query")

    class ApiError(message: String, apiCode: String? = null) : RequestError(
        "API Error: $message", apiCode = apiCode
    )

    class RateLimited : RequestError(
        "Rate limit exceeded", apiCode = "rateLimited"
    )

    class SourcesTooMany : RequestError(
        "Too many sources specified", apiCode = "sourcesTooMany"
    )

    class SourceDoesNotExist : RequestError(
        "Specified source does not exist", apiCode = "sourceDoesNotExist"
    )

    class BadRequest(message: String) : RequestError("Bad Request: $message")

    class Unauthorized : RequestError("Unauthorized access")

    class Forbidden : RequestError("Access is forbidden")

    class NotFound : RequestError("Requested resource not found")

    class InternalServerError : RequestError("Internal server error")

    class NetworkError(message: String) : RequestError("Network Error: $message")

    class UnknownError(
        message: String? = null, cause: Throwable? = null
    ) : RequestError(message, cause)

    companion object {

        fun errorHandler(cause: Throwable): RequestError = when (cause) {
            is ServerError -> when {
                cause.apiCode == "rateLimited" -> RateLimited()
                cause.apiCode == "sourcesTooMany" -> SourcesTooMany()
                cause.apiCode == "sourceDoesNotExist" -> SourceDoesNotExist()
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