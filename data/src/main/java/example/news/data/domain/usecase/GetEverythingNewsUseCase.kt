package example.news.data.domain.usecase

import example.news.data.data.remote.errors.RequestError
import example.news.data.data.remote.model.request.EverythingNewsRequest
import example.news.data.data.remote.model.response.NewsResponse
import example.news.data.data.remote.repository.NewsRepositoryImpl
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull

class GetEverythingUseCase(private val repository: NewsRepositoryImpl) {

    suspend operator fun invoke(request: EverythingNewsRequest): Result<NewsResponse> {

        if (request.searchRequest.isBlank()) return Result.failure(RequestError.EmptySearchQuery)
        return runCatching {
            repository.getEverythingNews(
                request.searchRequest,
                request.searchIn,
                request.fromDate,
                request.toDate,
                request.language,
                request.sortBy,
            )
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
