package example.news.data.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(news: List<NewsEntity>)

    @Query("SELECT * FROM news")
    suspend fun getAll(): List<NewsEntity>
}
