package example.news.data.domain.usecase

import example.news.data.data.remote.errors.RequestError
import example.news.data.data.remote.model.request.EverythingNewsRequest
import example.news.data.data.remote.model.response.NewsResponse
import example.news.data.data.remote.repository.NewsRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetEverythingUseCase(private val repository: NewsRepositoryImpl) {

    operator fun invoke(request: EverythingNewsRequest): Flow<NewsResponse> = flow {
        if (request.searchRequest.isBlank()) throw RequestError.EmptySearchQuery
        repository.getEverythingNews(
            request.searchRequest,
            request.searchIn,
            request.fromDate,
            request.toDate,
            request.language,
            request.sortBy?.name
        ).catch { cause -> throw cause }.collect { emit(it) }
    }
}
