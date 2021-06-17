package com.example.practicamaps.commons.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<viewBindingType : ViewBinding> : AppCompatActivity() {

    private val TAG = "BaseActivity"
    private var _binding: viewBindingType? = null

    // Varible de enlace que se utiliza para acceder a las vistas
    protected val binding
        get() = requireNotNull(_binding)

    abstract fun setupViewBinding(inflater: LayoutInflater): viewBindingType

    abstract fun init()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = setupViewBinding(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        init()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d(TAG, ":::OnDestroy::ViewBinding::$_binding:::")
    }

}