package example.news.data.domain.usecase

import example.news.data.errors.RequestError
import example.news.data.model.request.EverythingNewsRequest
import example.news.data.model.response.NewsResponse
import example.news.data.repository.NewsRepositoryImpl
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull

class GetEverythingUseCase(private val repository: NewsRepositoryImpl) {

    suspend operator fun invoke(request: EverythingNewsRequest): Result<NewsResponse> {

        if (request.searchRequest.isBlank()) return Result.failure(RequestError.EmptySearchQuery)
        val (q, searchIn, fromDate, toDate, language, sortBy) = request
        return runCatching {
            repository.getEverythingNews(q, searchIn, fromDate, toDate, language, sortBy)
                .catch { cause -> throw RequestError.errorHandler(cause) }
                .firstOrNull() ?: throw RequestError.UnknownError(
                "No data received from repository"
            )
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { e ->
                e.printStackTrace()
                Result.failure(
                    e as? RequestError ?: RequestError.UnknownError(e.message, e)
                )
            }
        )
    }
}
