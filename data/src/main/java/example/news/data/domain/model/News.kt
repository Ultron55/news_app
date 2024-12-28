package example.news.data.domain.model

import example.news.data.data.remote.model.response.Articles

data class News(
    val sourceName : String?,
    val author : String?,
    val title : String?,
    val description : String?,
    val url : String?,
    val imageUrl : String?,
    val publishedAt : String?,
) {
    companion object {
        fun from(articles: Articles) = News(
            articles.source.name,
            articles.author,
            articles.title,
            articles.description,
            articles.url,
            articles.urlToImage,
            articles.publishedAt.removeSuffix("Z").replace('T', ' ')
        )

        fun fromList(articles: List<Articles>?) = articles?.map { from(it) } ?: emptyList()



    }
}