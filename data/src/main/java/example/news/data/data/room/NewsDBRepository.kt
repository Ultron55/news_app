package example.news.data.data.room

import example.news.data.domain.model.News
import javax.inject.Inject

class NewsDBRepository @Inject constructor(private val newsDao: NewsDao) {

    suspend fun saveNews(newsList: List<News>) = newsList.map {
        NewsEntity(
            sourceName = it.sourceName,
            author = it.author,
            title = it.title,
            description = it.description,
            url = it.url,
            imageUrl = it.imageUrl,
            publishedAt = it.publishedAt
        )
    }.let { newsDao.insert(it) }


    suspend fun getNewsList() = newsDao.getAll().map {
        News(
            sourceName = it.sourceName,
            author = it.author,
            title = it.title,
            description = it.description,
            url = it.url,
            imageUrl = it.imageUrl,
            publishedAt = it.publishedAt
        )
    }
}