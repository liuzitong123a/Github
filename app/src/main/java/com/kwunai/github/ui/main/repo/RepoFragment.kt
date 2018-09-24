package com.kwunai.github.ui.main.repo

import com.kwunai.github.common.pager.FragmentPage
import com.kwunai.github.common.pager.GithubPagerFragment

/**
 * 仓库相关页面ViewPager载体
 * @author lzt
 */
class RepoFragment : GithubPagerFragment() {

    override fun getFragmentPagesLoggedIn(): List<FragmentPage> {
        return listOf(
                FragmentPage(RepoListFragment(), "我的"),
                FragmentPage(RepoListFragment(), "全部")
        )
    }

    override fun getFragmentPagesLoggedOut(): List<FragmentPage> {
        return listOf(FragmentPage(RepoListFragment(), "全部"))
    }
}