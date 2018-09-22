package com.kwunai.github.http.error

class TokenInvalidException(val authId: Int) : Exception("User Already logged in.")
