package example.news.data.data.network.api

import example.news.data.data.model.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything")
    suspend fun getEverythingNews(
        @Query("q") q : String,
        @Query("searchIn") searchIn : String?,
        @Query("from") fromDate : String?,
        @Query("to") toDate: String?,
        @Query("language") language: String?,
        @Query("sortBy") sortBy: String?
    ): NewsResponse

    @GET("top-headlines")
    suspend fun getTopHeadlinesNews(
        @Query("q") q: String,
        @Query("country") country: String?,
        @Query("category") category: String?,
        @Query("sources") sources: String?,
    ): NewsResponse

}