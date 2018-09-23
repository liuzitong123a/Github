package com.kwunai.github.entity

import com.orhanobut.logger.Logger
import retrofit2.Response
import java.util.regex.Pattern


@Suppress("unused")
sealed class GitHubPaging<T> {

    companion object {
        fun <T> create(error: Throwable): GitHubPagingError<T> {
            return GitHubPagingError(error.message ?: "unknown error")
        }

        fun <T> create(response: Response<T>): GitHubPaging<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) GitHubPagingEmpty() else
                    GitHubPagingSuccess(body = body, linkHeader = response.headers()?.get("link"))
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) response.message() else msg
                GitHubPagingError(errorMsg ?: "unknown error")
            }
        }
    }
}

class GitHubPagingEmpty<T> : GitHubPaging<T>()

data class GitHubPagingSuccess<T>(
        val body: T,
        val links: Map<String, String>
) : GitHubPaging<T>() {
    constructor(body: T, linkHeader: String?) : this(
            body = body,
            links = linkHeader?.extractLinks() ?: emptyMap()
    )

    val nextPage: Int? by lazy(LazyThreadSafetyMode.NONE) {
        links[NEXT_LINK]?.let { next ->
            val matcher = PAGE_PATTERN.matcher(next)
            if (!matcher.find() || matcher.groupCount() != 1) {
                null
            } else {
                try {
                    Integer.parseInt(matcher.group(1))
                } catch (ex: NumberFormatException) {
                    Logger.w("cannot parse next page from %s", next)
                    null
                }
            }
        }
    }

    companion object {
        private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private const val NEXT_LINK = "next"

        private fun String.extractLinks(): Map<String, String> {
            val links = mutableMapOf<String, String>()
            val matcher = LINK_PATTERN.matcher(this)

            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links[matcher.group(2)] = matcher.group(1)
                }
            }
            return links
        }

    }
}

data class GitHubPagingError<T>(val errorMessage: String) : GitHubPaging<T>()