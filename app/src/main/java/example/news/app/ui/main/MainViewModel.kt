package example.news.app.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import example.news.app.room.NewsDBRepository
import example.news.data.domain.model.News
import example.news.data.domain.usecase.GetEverythingUseCase
import example.news.data.data.errors.RequestError.ApiError
import example.news.data.data.errors.RequestError.BadRequest
import example.news.data.data.errors.RequestError.Forbidden
import example.news.data.data.errors.RequestError.InternalServerError
import example.news.data.data.errors.RequestError.NetworkError
import example.news.data.data.errors.RequestError.NotFound
import example.news.data.data.errors.RequestError.RateLimited
import example.news.data.data.errors.RequestError.SourceDoesNotExist
import example.news.data.data.errors.RequestError.SourcesTooMany
import example.news.data.data.errors.RequestError.Unauthorized
import example.news.data.data.errors.RequestError.UnknownError
import example.news.data.data.model.request.EverythingNewsRequest
import example.news.data.data.repository.NewsRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepositoryImpl: NewsRepositoryImpl,
    private val newsDBRepository: NewsDBRepository
) : ViewModel() {

    private val _news = MutableLiveData<List<News>>()
    val news: LiveData<List<News>> = _news
    private val _savedNews = MutableLiveData<List<News>>()
    val savedNews: LiveData<List<News>> = _savedNews
    val everythingNewsRequest = EverythingNewsRequest("")
    val isLoading = MutableLiveData<Boolean>()

    fun getEverythingNews() {
        val logTag = "getEverythingNews"
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            GetEverythingUseCase(newsRepositoryImpl).invoke(everythingNewsRequest)
                .onFailure {
                    Log.e("getEverythingNews", it.message.toString())
                    when (it) {
                        is RateLimited -> Log.d(logTag, "RateLimited")
                        is SourcesTooMany -> Log.d(logTag, "SourcesTooMany")
                        is SourceDoesNotExist -> Log.d(logTag, "SourceDoesNotExist")
                        is BadRequest -> Log.d(logTag, "BadRequest")
                        is Unauthorized -> Log.d(logTag, "Unauthorized")
                        is Forbidden -> Log.d(logTag, "Forbidden")
                        is NotFound -> Log.d(logTag, "NotFound")
                        is InternalServerError -> Log.d(logTag, "InternalServerError")
                        is NetworkError -> Log.d(logTag, "NetworkError")
                        is ApiError -> Log.d(logTag, "ApiError")
                        is UnknownError -> Log.d(logTag, "UnknownError")
                    }
                    isLoading.postValue(false)
                }.onSuccess {
                    Log.d(logTag, it.toString())
                    Log.d(logTag, "is null ${it.articles.isNullOrEmpty()}")
                    _news.postValue(News.fromList(it.articles))
                    isLoading.postValue(false)
                }
        }
    }

    fun saveNews(newsList: List<News>) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            newsDBRepository.saveNews(newsList)
            isLoading.postValue(false)
        }
    }

    fun getSavedNews() {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            _savedNews.postValue(newsDBRepository.getNewsList())
            isLoading.postValue(false)
        }

    }

}