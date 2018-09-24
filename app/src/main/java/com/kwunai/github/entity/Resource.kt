package com.kwunai.github.entity


@Suppress("unused")
sealed class Resource<out T> {

    companion object {
        fun <T> success(result: T): Resource<T> = Success(result)
        fun <T> loading(): Resource<T> = Loading
        fun <T> error(error: Throwable): Resource<T> = Error(error)
        fun <T> empty(): Resource<T> = Empty
    }

    object Loading : Resource<Nothing>()
    object Empty : Resource<Nothing>()
    data class Error(val error: Throwable) : Resource<Nothing>()
    data class Success<out T>(val result: T) : Resource<T>()
}