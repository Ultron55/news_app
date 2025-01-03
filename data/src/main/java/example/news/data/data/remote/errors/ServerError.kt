package example.news.data.data.remote.errors

import java.io.IOException

class ServerError(
    val code: Int,
    val apiCode : String?,
    val errorMessage: String? = null
): IOException(errorMessage) {
    companion object {
        var defaultMessage = "unexpectedError - This shouldn't happen, " +
                "and if it does then it's our fault, not yours. Try the request again shortly."
    }
}