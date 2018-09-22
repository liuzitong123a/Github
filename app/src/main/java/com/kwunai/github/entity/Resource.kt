package com.kwunai.github.entity


data class Resource<out T>(
        val status: Status,
        val data: T? = null,
        val throwable: Throwable? = null
) {
    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(status = Status.SUCCESS, data = data)
        }

        fun <T> error(throwable: Throwable): Resource<T> {
            return Resource(status = Status.ERROR, throwable = throwable)
        }

        fun <T> loading(): Resource<T> {
            return Resource(status = Status.LOADING)
        }
    }
}