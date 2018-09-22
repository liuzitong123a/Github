package com.kwunai.github.ext

import com.kwunai.github.GithubConstant
import java.util.regex.Pattern


fun checkEmail(mobile: String?): Boolean {
    return Pattern.compile(GithubConstant.EMAIL_PATTERN).matcher(mobile).matches()
}


fun checkPassword(password: String?): Boolean {
    return password.isNullOrEmpty() && password!!.length < 6
}