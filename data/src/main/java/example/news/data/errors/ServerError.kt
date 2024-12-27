package example.news.data.errors

import java.io.IOException

class ServerError(val code: Int, val errorMessage: String? = null): IOException(errorMessage) {
    companion object {
        val userCodes = listOf("rateLimited", "sourcesTooMany", "sourceDoesNotExist")
        var defaultMessage = "unexpectedError - This shouldn't happen, " +
                "and if it does then it's our fault, not yours. Try the request again shortly."
    }
}