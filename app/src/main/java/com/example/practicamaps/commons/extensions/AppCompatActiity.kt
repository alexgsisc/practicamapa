package com.example.practicamaps.commons.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.replaceFragment(fragment: Fragment, tag: String? = null, container: Int, transition: Int = FragmentTransaction.TRANSIT_NONE, backStack: String? = null) {
    supportFragmentManager
        .beginTransaction()
        .setTransition(transition)
        .replace(container, fragment, tag)
        .addToBackStack(backStack)
        .commitAllowingStateLoss()
}

fun AppCompatActivity.addFragment(fragment: Fragment, tag: String? = null, container: Int, transition: Int = FragmentTransaction.TRANSIT_NONE, backStack: String? = null) {
    supportFragmentManager
        .beginTransaction()
        .setTransition(transition)
        .add(container, fragment, tag)
        .addToBackStack(backStack)
        .commitAllowingStateLoss()
}