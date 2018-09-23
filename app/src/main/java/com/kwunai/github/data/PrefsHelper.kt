package com.kwunai.github.data

import android.content.SharedPreferences
import com.kwunai.github.entity.UserRsp
import com.kwunai.github.utils.*

class PrefsHelper(prefs: SharedPreferences) {

    init {
        prefs.initKey("kwunai_github_v1")
    }

    var authId: Int by prefs.int(isEncrypt = true)
    var username: String by prefs.string(isEncrypt = true)
    var password: String by prefs.string(isEncrypt = true)
    var token: String by prefs.string(isEncrypt = true)

    var user: UserRsp? by prefs.gson<UserRsp?>(null)

    fun isLoggedIn(): Boolean = token.isNotEmpty()


    var navigationLastSelected: Int by prefs.int()
}