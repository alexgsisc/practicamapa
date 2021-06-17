package com.example.practicamaps

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.practicamaps.commons.MapsApplication
import com.example.practicamaps.commons.base.BaseActivity
import com.example.practicamaps.commons.database.AppDataBase
import com.example.practicamaps.commons.extensions.addFragment
import com.example.practicamaps.databinding.ActivityMainBinding
import com.example.practicamaps.maps.presentation.ui.FragmentMaps
import com.example.practicamaps.maps.presentation.ui.ListRouterUser

class MainActivity : BaseActivity<ActivityMainBinding>() {


    override fun setupViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun init() {
        dataBase = (application as MapsApplication).database

        binding.bnvApplication.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_run -> {
                    val fragment = FragmentMaps.newInstance()
                    openFragment(fragment)
                    true
                }
                R.id.navigation_history -> {
                    val fragment = ListRouterUser.newInstance()
                    openFragment(fragment)
                    true
                }
                else -> false
            }
        }

        binding.bnvApplication.selectedItemId = R.id.navigation_run
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.root_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        var dataBase: AppDataBase? = null
    }
}