package com.kwunai.github


object GithubConstant {

    const val LOGGER_TAG = "github"

    const val BASE_API = "https://api.github.com"
    const val READ_TIMEOUT = 10L
    const val WRITE_TIMEOUT = 10L
    const val CONNECT_TIMEOUT = 10L

    const val EMAIL_PATTERN = " ^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}\$"

    const val QUIT_TIME = 2L
}