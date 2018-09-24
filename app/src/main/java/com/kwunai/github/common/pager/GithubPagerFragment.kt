package com.kwunai.github.common.pager

import android.os.Bundle
import android.view.View
import com.kwunai.github.GithubApp
import com.kwunai.github.R
import com.kwunai.github.common.GithubFragment
import com.kwunai.github.data.PrefsHelper
import com.kwunai.github.databinding.FragmentPagerBinding
import com.kwunai.github.entity.UserRsp
import com.kwunai.github.repo.AuthRepository
import com.kwunai.github.repo.OnAuthStateChangeListener
import com.kwunai.github.ui.main.MainActivity
import com.kwunai.github.ui.main.mainModule
import kotlinx.android.synthetic.main.fragment_pager.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance


abstract class GithubPagerFragment : GithubFragment<FragmentPagerBinding>(),
        GithubPagerConfig, OnAuthStateChangeListener {

    override val layoutId: Int = R.layout.fragment_pager

    override val kodein: Kodein = Kodein.lazy {
        extend(GithubApp.INSTANCE.kodein)
        import(mainModule)
        bind<GithubPagerFragment>() with instance(this@GithubPagerFragment)
    }

    private val adapter by lazy {
        GithubPagerAdapter(childFragmentManager)
    }

    private val authRepository: AuthRepository by instance()

    protected val helper: PrefsHelper by instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewPager.adapter = adapter
        (activity as MainActivity).actionBarController.setupWithViewPager(mViewPager)
        adapter.fragmentPages.addAll(
                if (helper.isLoggedIn()) {
                    getFragmentPagesLoggedIn()
                } else {
                    getFragmentPagesLoggedOut()
                }
        )
        authRepository.onAuthStateChangeListeners.add(this)
    }

    override fun onLogin(user: UserRsp) {
        adapter.fragmentPages.clear()
        adapter.fragmentPages.addAll(getFragmentPagesLoggedIn())
    }

    override fun onLogout() {
        adapter.fragmentPages.clear()
        adapter.fragmentPages.addAll(getFragmentPagesLoggedOut())
    }

    override fun onDestroy() {
        super.onDestroy()
        authRepository.onAuthStateChangeListeners.remove(this)
    }

}