package example.news.data

import app.cash.turbine.test
import com.google.gson.Gson
import example.news.data.data.remote.model.response.Articles
import example.news.data.data.remote.model.response.NewsResponse
import example.news.data.data.remote.model.response.Source
import example.news.data.data.remote.network.api.NewsApi
import example.news.data.data.remote.repository.NewsRepositoryImpl
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsRepositoryImplTest {

    private var mockWebServer = MockWebServer()
    private lateinit var newsApi: NewsApi
    private lateinit var repository: NewsRepositoryImpl

    @Before
    fun setUp() {
        mockWebServer.start()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsApi = retrofit.create(NewsApi::class.java)
        repository = NewsRepositoryImpl(newsApi)
    }

    @After
    fun tearDown() = mockWebServer.shutdown()

    @Test
    fun getEverythingNews_ShouldReturnValidNewsResponse() = runBlocking {
        val mockArticles = listOf(createArticle(0), createArticle(1))
        mockWebServer.enqueue(
            MockResponse()
                .setBody(Gson().toJson(
                    NewsResponse("ok", null, null, 2, mockArticles)
                ))
                .setResponseCode(200)
        )
        repository.getEverythingNews(
            q = "kotlin",
            searchIn = null,
            fromDate = null,
            toDate = null,
            language = null,
            sortBy = null
        ).test {
            val result = awaitItem()
            assertEquals("ok", result.status)
            assertEquals(2, result.totalResults)
            assertEquals("Title0", result.articles?.get(0)?.title)
            assertEquals("Source1", result.articles?.get(1)?.source?.name)
            awaitComplete()
        }
    }

    @Test
    fun getEverythingNews_ShouldReturnErrorResult(): Unit = runBlocking {
        val message =
            "Required parameters are missing, the scope of your search is too broad. " +
                    "Please set any of the following required parameters and try again: " +
                    "q, qInTitle, sources, domains."
        mockWebServer.enqueue(
                MockResponse()
                    .setBody(Gson().toJson(
                        NewsResponse(
                            "error",
                            "parametersMissing",
                            message,
                            null,
                            null)
                    ))
                    .setResponseCode(400)
                )
        repository.getEverythingNews(
            q = "",
            searchIn = null,
            fromDate = null,
            toDate = null,
            language = null,
            sortBy = null
        ).test {
            val error = awaitError()
            assertEquals("Bad Request: $message", error.message)
        }
    }

    private fun createArticle(index: Int) = Articles(
        Source(index.toString(), "Source$index"),
        "Author$index",
        "Title$index",
        "Description$index",
        "url$index",
        "imageUrl$index",
        "publishedAt$index"
    )
}