package example.news.app.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import example.news.data.domain.usecase.GetEverythingUseCase
import example.news.data.errors.RequestError.ApiError
import example.news.data.errors.RequestError.BadRequest
import example.news.data.errors.RequestError.Forbidden
import example.news.data.errors.RequestError.InternalServerError
import example.news.data.errors.RequestError.NetworkError
import example.news.data.errors.RequestError.NotFound
import example.news.data.errors.RequestError.RateLimited
import example.news.data.errors.RequestError.SourceDoesNotExist
import example.news.data.errors.RequestError.SourcesTooMany
import example.news.data.errors.RequestError.Unauthorized
import example.news.data.errors.RequestError.UnknownError
import example.news.data.model.request.EverythingNewsRequest
import example.news.data.repository.NewsRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepositoryImpl: NewsRepositoryImpl
) : ViewModel() {
    
    fun getEverythingNews() {
        val logTag = "getEverythingNews"
        viewModelScope.launch(Dispatchers.IO) {
            GetEverythingUseCase(newsRepositoryImpl).invoke(
                EverythingNewsRequest("Apple")
            ).onFailure {
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
            }.onSuccess {
                Log.d(logTag, it.toString())
            }
        }
    }
}