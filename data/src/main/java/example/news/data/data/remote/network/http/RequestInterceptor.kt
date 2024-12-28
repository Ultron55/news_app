package example.news.data.data.remote.network.http

import example.news.data.BuildConfig
import okhttp3.Interceptor

class RequestInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain) =
        chain.proceed(with(chain.request().newBuilder()) {
            addHeader("Authorization", BuildConfig.API_KEY)
            build()
        })
}