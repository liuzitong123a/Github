package com.kwunai.github.ui.main.repo

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.kwunai.github.GithubApp
import com.kwunai.github.R
import com.kwunai.github.common.GithubFragment
import com.kwunai.github.databinding.FragmentRepoBinding
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance

/**
 * 仓库相关页面ViewPager载体
 * @author lzt
 */
class RepoFragment : GithubFragment<FragmentRepoBinding>() {


    override val layoutId: Int = R.layout.fragment_repo

    override val kodein: Kodein = Kodein.lazy {
        extend(GithubApp.INSTANCE.kodein)
        import(repoModule)
        bind<RepoFragment>() with instance(this@RepoFragment)
    }

    private val viewModel: RepoViewModel by instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
    }


}