package com.kwunai.github.common

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein

abstract class GithubActivity<B : ViewDataBinding> : AppCompatActivity(), KodeinAware {

    protected lateinit var binding: B

    abstract val layoutId: Int

    protected val parent: Kodein by closestKodein()

    override val kodein: Kodein by retainedKodein {
        extend(parent, copy = Copy.All)
    }

    override val kodeinTrigger = KodeinTrigger()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        binding = DataBindingUtil.setContentView(this, layoutId)
        with(binding) {
            setLifecycleOwner(this@GithubActivity)
        }
        kodeinTrigger.trigger()
    }

}