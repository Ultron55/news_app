package example.news.data.repository

import example.news.data.domain.repository.NewsRepository
import example.news.data.network.api.NewsApi
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val newsApi: NewsApi) : NewsRepository {

    override suspend fun getEverythingNews(
        q : String,
        searchIn : String?,
        fromDate : String?,
        toDate : String?,
        language : String?,
        sortBy : String?,
    ) = wrapRequest { newsApi.getEverythingNews(q, searchIn, fromDate, toDate, language, sortBy) }

    override suspend fun getTopHeadlinesNews(
        q : String,
        country: String?,
        category: String?,
        sources: String?,
    ) =
        wrapRequest { newsApi.getTopHeadlinesNews(q, country, category, sources) }
}