package com.kwunai.github.common

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger


abstract class GithubFragment<B : ViewDataBinding> : Fragment(), KodeinAware {

    protected lateinit var binding: B

    override val kodeinTrigger = KodeinTrigger()

    abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        with(binding) {
            setLifecycleOwner(this@GithubFragment)
        }
        kodeinTrigger.trigger()
    }

}