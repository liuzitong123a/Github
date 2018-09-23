package com.kwunai.github.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import com.kwunai.github.GithubConstant
import com.kwunai.github.R
import com.kwunai.github.common.GithubActivity
import com.kwunai.github.data.PrefsHelper
import com.kwunai.github.databinding.ActivityMainBinding
import com.kwunai.github.entity.UserRsp
import com.kwunai.github.ext.*
import com.kwunai.github.ui.login.LoginActivity
import com.kwunai.repo.AuthRepository
import com.kwunai.repo.OnAuthStateChangeListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.kodein.di.Kodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance

/**
 * @author lzt
 * github主界面
 */
class MainActivity : GithubActivity<ActivityMainBinding>(), OnAuthStateChangeListener {

    private var isQuit = false

    override val layoutId: Int = R.layout.activity_main

    override val kodein: Kodein by retainedKodein {
        extend(parent)
        import(mainModule)
        bind<MainActivity>() with instance(this@MainActivity)
    }

    private val authRepository: AuthRepository by instance()

    private val mainViewModal: MainViewModal by instance()

    private val helper: PrefsHelper by instance()

    private val navigationController by lazy {
        NavigationController(helper, navigationView, ::onNavItemChanged, ::handleNavigationHeaderClickEvent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = mainViewModal
        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        initNavigationView()
        authRepository.onAuthStateChangeListeners.add(this)
        mainViewModal.error.observe(this, Observer { toast("注销失败") })
        mainViewModal.logout.observe(this, Observer { toast("注销成功") })
    }

    override fun onLogin(user: UserRsp) {
        navigationController.useLoginLayout()
    }

    override fun onLogout() {
        navigationController.useNoLoginLayout()
    }

    /**
     * 登录登出切换对应的menu
     */
    private fun initNavigationView() {
        helper.isLoggedIn()
                .yes {
                    navigationController.useLoginLayout()
                }
                .otherwise {
                    navigationController.useNoLoginLayout()
                }
        navigationController.selectProperItem()
    }

    /**
     * 当navigationView的menu切换时触发
     */
    private fun onNavItemChanged(navViewItem: NavViewItem) {
        drawerLayout.afterClosed {
            showFragment(R.id.fl_container, navViewItem.fragmentClass, navViewItem.arguments)
            title = navViewItem.title
        }
    }

    /**
     * 点击drawerLayout头部触发，处理登出和登录操作
     */
    private fun handleNavigationHeaderClickEvent() {
        helper.isLoggedIn().no {
            startActivity<LoginActivity>()
        }.otherwise {
            alert("提示", "确认注销吗?") {
                yesButton { _ -> mainViewModal.logout() }
                noButton { toast("取消了") }
            }.show()
        }
    }

    /**
     * 当drawerLayout出现时，点击返回应该先让drawerLayout收回去
     */
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (!isQuit) {
                toast(message = "请再按一次退出")
                isQuit = true
                delayTimer(GithubConstant.QUIT_TIME) {
                    isQuit = false
                }
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        authRepository.onAuthStateChangeListeners.remove(this)
    }
}
