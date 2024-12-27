package example.news.data.domain.repository

import com.google.gson.Gson
import example.news.data.model.response.NewsResponse
import example.news.data.errors.ServerError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

interface BaseRepository {

    fun <T> wrapRequest(a: suspend () -> T): Flow<T> =
        flow {
            runCatching { emit(a.invoke()) }
                .onFailure {
                    if (it is HttpException) handleHttpException(it)
                    else throw it
                }
        }
            .flowOn(Dispatchers.IO)

    fun handleHttpException(exception: HttpException) {
        val code = exception.code()
        throw runCatching {
            Gson().fromJson(
                exception.response()?.errorBody()?.charStream(),
                NewsResponse::class.java)
        }.fold(
            {
                throw ServerError(
                    code,
                    if (it.code in ServerError.userCodes) it.message
                    else ServerError.defaultMessage
                )
            },
            { exception }
        )
    }

}