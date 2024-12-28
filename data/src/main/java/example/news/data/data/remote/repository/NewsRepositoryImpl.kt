package example.news.data.data.remote.repository

import example.news.data.data.remote.network.api.NewsApi
import example.news.data.domain.repository.NewsRemoteRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val newsApi: NewsApi) : NewsRemoteRepository {

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
    ) = wrapRequest { newsApi.getTopHeadlinesNews(q, country, category, sources) }
}