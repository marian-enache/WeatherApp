package com.example.weatherapp.utils

import androidx.compose.runtime.Stable
import com.example.weatherapp.utils.Resource.Status.*

@Stable
class Resource<T> private constructor(val status: Status, val data: T?, val throwable: Throwable?) {

    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        fun <T> success(data: T): Resource<T> = Resource(SUCCESS, data, null)
        fun <T> error(throwable: Throwable? = null, data: T? = null): Resource<T> = Resource(ERROR, data, throwable)
        fun <T> loading(data: T? = null): Resource<T> = Resource(LOADING, data, null)
    }
}
