package example.news.data.domain.model

import android.util.Log
import example.news.data.model.response.Articles

data class News(
    val sourceName : String?,
    val author : String?,
    val title : String?,
    val description : String?,
    val url : String?,
    val imageUrl : String?,
) {
    companion object {
        fun from(articles: Articles) = News(
            articles.source.name,
            articles.author,
            articles.title,
            articles.description,
            articles.url,
            articles.urlToImage,
        )

        fun fromList(articles: List<Articles>?) = articles?.map { from(it) } ?: emptyList()

    }
}