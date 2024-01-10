package com.example.weatherapp.framework.networking

import com.example.weatherapp.utils.NoNetworkException
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import javax.inject.Inject

class NetworkAvailabilityInterceptor @Inject constructor(
    private val networkHandler: NetworkHandler
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!networkHandler.isNetworkAvailable())
            throw NoNetworkException()

        return try {
            chain.proceed(chain.request())
        } catch (e: IOException) {
            throw NoNetworkException()
        }
    }
}