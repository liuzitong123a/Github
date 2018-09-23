package com.kwunai.github.ui.main

import android.support.design.widget.NavigationView
import android.view.MenuItem
import com.kwunai.github.R
import com.kwunai.github.data.PrefsHelper
import com.kwunai.github.entity.UserRsp
import com.kwunai.github.ext.doOnLayoutAvailable
import com.kwunai.github.ext.loadWithGlide
import com.kwunai.github.ext.selectItem
import kotlinx.android.synthetic.main.nav_header_main.view.*

/**
 * 控制NavigationView的变化
 */
class NavigationController(
        private val helper: PrefsHelper,
        private val navigationView: NavigationView,
        private val onNavItemChanged: (NavViewItem) -> Unit,
        private val onHeaderClick: () -> Unit
) : NavigationView.OnNavigationItemSelectedListener {

    private var currentItem: NavViewItem? = null

    init {
        navigationView.setNavigationItemSelectedListener(this)
    }

    /**
     * 当被选中item发生改变时的监听
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navigationView.apply {
            helper.navigationLastSelected = item.itemId
            val navItem = NavViewItem[item.itemId]
            onNavItemChanged(navItem)
        }

        return true
    }

    /**
     * 登录下navigationView的menu
     */
    fun useLoginLayout() {
        navigationView.menu.clear()
        navigationView.inflateMenu(R.menu.menu_drawer)
        onUpdate(helper.user)
        selectProperItem()
    }

    /**
     * 未登录状态下navigationView的menu
     */
    fun useNoLoginLayout() {
        navigationView.menu.clear()
        navigationView.inflateMenu(R.menu.menu_drawer_no_logged_in)
        onUpdate(helper.user)
        selectProperItem()
    }

    /**
     * 当登录状态发生改变时改变对应的UI
     */
    private fun onUpdate(user: UserRsp?) {
        navigationView.doOnLayoutAvailable {
            navigationView.apply {
                tvUsername.text = user?.login ?: "请登录"
                tvHome.text = user?.html_url ?: "kwunai@android.cn"
                if (user == null) {
                    avatarView.setImageResource(R.drawable.ic_github)
                } else {
                    avatarView.loadWithGlide(user.avatar_url, user.login.first())
                }

                navigationHeader.setOnClickListener { onHeaderClick() }
            }
        }
    }

    /**
     * 当前默认选中
     */
    fun selectProperItem() {
        navigationView.doOnLayoutAvailable {
            ((currentItem?.let { NavViewItem[it] }
                    ?: helper.navigationLastSelected)
                    .takeIf { navigationView.menu.findItem(it) != null }
                    ?: run { R.id.navRepos })
                    .let(navigationView::selectItem)
        }
    }
}