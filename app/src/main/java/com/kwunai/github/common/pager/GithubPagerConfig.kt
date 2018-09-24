package com.kwunai.github.common.pager

import android.support.v4.app.Fragment

interface GithubPagerConfig {

    fun getFragmentPagesLoggedIn(): List<FragmentPage>

    fun getFragmentPagesLoggedOut(): List<FragmentPage>

}

data class FragmentPage(val fragment: Fragment, val title: String)