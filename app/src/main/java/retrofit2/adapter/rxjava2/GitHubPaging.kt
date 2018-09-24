package retrofit2.adapter.rxjava2

import com.orhanobut.logger.Logger
import okhttp3.HttpUrl

class GitHubPaging<T> : ArrayList<T>() {
    companion object {
        const val URL_PATTERN = """(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"""
    }

    private val relMap = HashMap<String, String?>().withDefault { null }

    private val last by relMap
    private val next by relMap

    val isLast
        get() = last == null

    val hasNext
        get() = next != null

    var since: Int = 0

    operator fun get(key: String): String? {
        return relMap[key]
    }

    fun setupLinks(link: String) {
        Logger.w("setupLinks: $link")
        Regex("""<($URL_PATTERN)>; rel="(\w+)"""").findAll(link).asIterable()
                .map { matchResult ->
                    val url = matchResult.groupValues[1]
                    relMap[matchResult.groupValues[3]] = url // next=....
                    if (url.contains("since")) {
                        HttpUrl.parse(url)?.queryParameter("since")?.let {
                            since = it.toInt()
                        }
                    }
                    Logger.w("${matchResult.groupValues[3]} => ${matchResult.groupValues[1]}")
                }
    }

    fun mergeData(paging: GitHubPaging<T>): GitHubPaging<T> {
        addAll(paging)
        relMap.clear()
        relMap.putAll(paging.relMap)
        since = paging.since
        return this
    }
}