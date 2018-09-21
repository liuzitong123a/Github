package com.kwunai.github

import com.kwunai.github.ext.getDeviceId

object GithubConfig {

    val SCOPES = listOf("user", "repo", "notifications", "gist", "admin:org")
    const val clientId = "aa267a3149ab8350add7"
    const val clientSecret = "4ade8262d40088bb2553b85e55c08be52e4a862c"
    const val note = "kotlin.cn"
    const val noteUrl = "https://github.com/liuzitong123a"

    val fingerPrint by lazy {
        (GithubApp.INSTANCE.getDeviceId() + clientId)
    }
}