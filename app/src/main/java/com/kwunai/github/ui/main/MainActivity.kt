package com.kwunai.github.ui.main

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import com.kwunai.github.GithubConstant
import com.kwunai.github.R
import com.kwunai.github.common.GithubActivity
import com.kwunai.github.data.PrefsHelper
import com.kwunai.github.databinding.ActivityMainBinding
import com.kwunai.github.ext.delayTimer
import com.kwunai.github.ext.toast
import org.kodein.di.Kodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance

class MainActivity : GithubActivity<ActivityMainBinding>() {

    private var isQuit = false

    override val layoutId: Int = R.layout.activity_main

    override val kodein: Kodein by retainedKodein {
        extend(parent)
        bind<MainActivity>() with instance(this@MainActivity)
    }

    private val helper: PrefsHelper by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.user = helper.user
        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else {
            if (!isQuit) {
                toast(message = "再按一次退出程序")
                isQuit = true
                delayTimer(GithubConstant.QUIT_TIME) {
                    isQuit = false
                }
            } else {
                super.onBackPressed()
            }
        }
    }
}
