package example.news.app.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import example.news.data.data.remote.errors.RequestError
import example.news.data.data.room.NewsDBRepository
import example.news.data.domain.model.News
import example.news.data.domain.usecase.GetEverythingUseCase
import example.news.data.data.remote.errors.RequestError.ApiError
import example.news.data.data.remote.errors.RequestError.BadRequest
import example.news.data.data.remote.errors.RequestError.Forbidden
import example.news.data.data.remote.errors.RequestError.InternalServerError
import example.news.data.data.remote.errors.RequestError.NetworkError
import example.news.data.data.remote.errors.RequestError.NotFound
import example.news.data.data.remote.errors.RequestError.RateLimited
import example.news.data.data.remote.errors.RequestError.SourceDoesNotExist
import example.news.data.data.remote.errors.RequestError.SourcesTooMany
import example.news.data.data.remote.errors.RequestError.Unauthorized
import example.news.data.data.remote.errors.RequestError.UnknownError
import example.news.data.data.remote.model.request.EverythingNewsRequest
import example.news.data.data.remote.repository.NewsRepositoryImpl
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
    val errorMessages = MutableLiveData<String>()

    fun getEverythingNews() {
        val logTag = "getEverythingNews"
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            GetEverythingUseCase(newsRepositoryImpl).invoke(everythingNewsRequest)
                .onFailure {
                    Log.e(logTag, it.message.toString())
                    if (it is RequestError) {
                        logRequestError(it, logTag)
                        when (it) {
                            is RateLimited -> {}
                            is SourcesTooMany -> {}
                            is SourceDoesNotExist -> {}
                            is BadRequest -> {}
                            is Unauthorized -> {}
                            is Forbidden -> {}
                            is NotFound -> {}
                            is InternalServerError -> {}
                            is NetworkError -> {}
                            is ApiError -> {}
                            is UnknownError -> {}
                            else -> {}
                        }
                    } else Log.d(logTag, "UnknownError")
                    isLoading.postValue(false)
                }.onSuccess {
                    Log.d(logTag, it.toString())
                    Log.d(logTag, "is null ${it.articles.isNullOrEmpty()}")
                    _news.postValue(News.fromList(it.articles))
                    isLoading.postValue(false)
                }
        }
    }

    private fun logRequestError(error: RequestError, logTag : String) {
        Log.d(logTag, error.getClassName())
        errorMessages.postValue(error.message)
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