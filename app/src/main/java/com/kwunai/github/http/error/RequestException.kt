package com.kwunai.github.http.error

import java.lang.Exception

class RequestException(val error: String) : Exception()