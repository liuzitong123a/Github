package com.kwunai.github.ui.main

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import com.kwunai.github.R
import com.kwunai.github.R.id
import com.kwunai.github.ui.main.about.AboutFragment
import com.kwunai.github.ui.main.issue.IssueFragment
import com.kwunai.github.ui.main.people.PeopleFragment
import com.kwunai.github.ui.main.repo.RepoFragment

class NavViewItem(
        private val groupId: Int = 0,
        val title: String,
        @DrawableRes val icon: Int,
        val fragmentClass: Class<out Fragment>,
        val arguments: Bundle = Bundle()
) {

    companion object {
        private val items = mapOf(
                R.id.navRepos to NavViewItem(0, "Repository", R.drawable.ic_repository, RepoFragment::class.java),
                R.id.navPeople to NavViewItem(0, "People", R.drawable.ic_people, PeopleFragment::class.java),
                R.id.navIssue to NavViewItem(0, "Issue", R.drawable.ic_issue, IssueFragment::class.java),
                R.id.navAbout to NavViewItem(0, "About", R.drawable.ic_about_us, AboutFragment::class.java)
        )

        operator fun get(@IdRes navId: Int): NavViewItem {

            return items[navId] ?: items[id.navRepos]!!
        }

        operator fun get(item: NavViewItem): Int {
            return items.filter { it.value == item }.keys.first()
        }
    }

    override fun toString(): String {
        return "NavViewItem(groupId=$groupId, title='$title', icon=$icon, fragmentClass=$fragmentClass, arguments=$arguments)"
    }
}