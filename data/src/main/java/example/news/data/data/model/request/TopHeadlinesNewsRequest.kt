package example.news.data.data.model.request

data class TopHeadlinesNewsRequest(
    val searchRequest: String,
    val country : String,
    val category : String? = null,
    val sources : String? = null,
) {
    companion object {
        val categories = listOf(
            "business", "entertainment", "general", "health", "science", "sports", "technology"
        )
    }
}