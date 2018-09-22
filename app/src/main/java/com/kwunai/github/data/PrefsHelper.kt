package com.kwunai.github.data

import android.content.SharedPreferences
import com.kwunai.github.utils.boolean
import com.kwunai.github.utils.initKey
import com.kwunai.github.utils.int
import com.kwunai.github.utils.string

class PrefsHelper(prefs: SharedPreferences) {

    init {
        // 初始化密钥，且密钥是16位的
        prefs.initKey("kwunai_github_v1")
    }

    var authId: Int by prefs.int(isEncrypt = true)
    var username: String by prefs.string(isEncrypt = true)
    var password: String by prefs.string(isEncrypt = true)
    var token: String by prefs.string(isEncrypt = true)

    fun isLoggedIn(): Boolean = token.isNotEmpty()
}