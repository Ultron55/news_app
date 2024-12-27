package example.news.data.model.request

import java.text.SimpleDateFormat

data class EverythingNewsRequest (
    val searchRequest: String,
    val searchIn: String? = null,
    val fromDate: String? = null,
    val toDate: String? = null,
    val language: String? = null,
    val sortBy: String? = null,
) {
    @Suppress("SimpleDateFormat")
    companion object {
        val searchIn = listOf("title", "description", "content")
        val languages = listOf(
            "ar", "de", "en", "es", "fr", "he", "it", "nl", "no", "pt", "ru", "se", "ud", "zh"
        )
        val sortBy = listOf("relevancy", "popularity", "publishedAt")
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    }
}