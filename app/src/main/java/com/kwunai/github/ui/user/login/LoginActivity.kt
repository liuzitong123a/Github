package com.kwunai.github.ui.user.login

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.kwunai.github.R
import com.kwunai.github.common.GithubActivity
import com.kwunai.github.databinding.ActivityLoginBinding
import com.kwunai.github.entity.UserRsp
import com.kwunai.github.ext.toast
import org.kodein.di.Kodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance


class LoginActivity : GithubActivity<ActivityLoginBinding>() {

    override val layoutId: Int = R.layout.activity_login

    override val kodein: Kodein by retainedKodein {
        extend(parent)
        import(loginModule)
        bind<LoginActivity>() with instance(this@LoginActivity)
    }

    private val viewModel: LoginViewModel by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        viewModel.error.observe(this, Observer<Throwable> {
            toast(it?.message ?: "登录失败")
        })
        viewModel.user.observe(this, Observer<UserRsp> { it ->
            it?.let {
                toast("登录成功")
            }
        })
    }
}