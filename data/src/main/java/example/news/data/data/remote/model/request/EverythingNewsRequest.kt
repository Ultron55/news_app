package example.news.data.data.remote.model.request

import java.text.SimpleDateFormat

class EverythingNewsRequest (
    var searchRequest: String,
    var searchIn: String? = null,
    var fromDate: String? = null,
    var toDate: String? = null,
    var language: String? = null,
    var sortBy: SortBy? = null,
) {
    sealed class SortBy(val name: String) {
        data object PublishedAt : SortBy("publishedAt")
        data object Relevancy : SortBy("relevancy")
        data object Popularity : SortBy("popularity")
    }

    @Suppress("SimpleDateFormat")
    companion object {
        val searchIn = listOf("title", "description", "content")
        val languages = listOf(
            "ar", "de", "en", "es", "fr", "he", "it", "nl", "no", "pt", "ru", "se", "ud", "zh"
        )
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    }
}