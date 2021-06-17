package com.example.practicamaps.commons.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<viewBinding : ViewBinding> :
    Fragment() {

    private val TAG = "BaseFragment"

    private var _binding: viewBinding? = null

    protected val binding
        get() = requireNotNull(_binding)


    abstract fun setupViewBinding(inflater: LayoutInflater, container: ViewGroup?): viewBinding

    abstract fun init()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = setupViewBinding(inflater, container)
        return requireNotNull(_binding).root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d(TAG, ":::OnDestroy::ViewBinding::$_binding:::")
    }
}