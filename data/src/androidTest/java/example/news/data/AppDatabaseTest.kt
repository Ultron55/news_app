package example.news.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import example.news.data.data.room.AppDatabase
import example.news.data.data.room.NewsDao
import example.news.data.data.room.NewsEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AppDatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var newsDao: NewsDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        newsDao = database.newsDao()
    }

    @After
    fun teardown() = database.close()

    @Test
    fun insertAndRetrieveNews() = runBlocking {
        val newsItem = NewsEntity(
            id = 0,
            sourceName = "Test Source",
            author = "John Doe",
            title = "Breaking News",
            description = "This is a test news description.",
            url = "http://example.com",
            imageUrl = "http://example.com/image.jpg",
            publishedAt = "2024-12-27"
        )
        newsDao.insert(listOf(newsItem))
        val retrievedNews = newsDao.getAll()
        assertEquals(1, retrievedNews.size)
        assertEquals(newsItem.sourceName, retrievedNews[0].sourceName)
        assertEquals(newsItem.author, retrievedNews[0].author)
        assertEquals(newsItem.title, retrievedNews[0].title)
        assertEquals(newsItem.description, retrievedNews[0].description)
        assertEquals(newsItem.url, retrievedNews[0].url)
        assertEquals(newsItem.imageUrl, retrievedNews[0].imageUrl)
        assertEquals(newsItem.publishedAt, retrievedNews[0].publishedAt)
    }
}