package com.kwunai.github.data

import android.content.SharedPreferences
import com.kwunai.github.utils.boolean
import com.kwunai.github.utils.initKey
import com.kwunai.github.utils.string

class PrefsHelper(private val prefs: SharedPreferences) {

    init {
        // 初始化密钥，且密钥是16位的
        prefs.initKey("kwunai_github_123")
    }

    var username: String by prefs.string(isEncrypt = true)
    var password: String by prefs.string(isEncrypt = true)
    var isLoggedIn: Boolean by prefs.boolean(isEncrypt = true)
    var token: String by prefs.string(isEncrypt = true)
}