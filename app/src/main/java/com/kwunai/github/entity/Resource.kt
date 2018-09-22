package com.kwunai.github.entity


sealed class Resource<out T> {

    companion object {
        fun <T> success(result: T): Resource<T> = Success(result)
        fun <T> loading(): Resource<T> = Loading
        fun <T> error(error: Throwable): Resource<T> = Error(error)
    }

    object Loading : Resource<Nothing>()
    data class Error(val error: Throwable) : Resource<Nothing>()
    data class Success<out T>(val result: T) : Resource<T>()
}