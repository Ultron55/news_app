package example.news.data.data.remote.model.request

import java.text.SimpleDateFormat

class EverythingNewsRequest (
    var searchRequest: String,
    var searchIn: String? = null,
    var fromDate: String? = null,
    var toDate: String? = null,
    var language: String? = null,
    var sortBy: String? = null,
) {
    @Suppress("SimpleDateFormat")
    companion object {
        val searchIn = listOf("title", "description", "content")
        val languages = listOf(
            "ar", "de", "en", "es", "fr", "he", "it", "nl", "no", "pt", "ru", "se", "ud", "zh"
        )
        val sortBy = listOf("publishedAt", "relevancy", "popularity")
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    }
}