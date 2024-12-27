package example.news.data.domain.repository

import example.news.data.model.response.NewsResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository : BaseRepository {
    suspend fun getEverythingNews(
        q : String,
        searchIn : String?,
        fromDate : String?,
        toDate : String?,
        language : String?,
        sortBy : String?,
    ): Flow<NewsResponse>
    suspend fun getTopHeadlinesNews(
        q : String,
        country: String?,
        category: String?,
        sources: String?,
    ) : Flow<NewsResponse>
}