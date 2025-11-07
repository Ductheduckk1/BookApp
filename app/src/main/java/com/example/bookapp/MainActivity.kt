package com.example.bookapp

import android.content.Intent
import android.os.Bundle
import com.example.bookapp.core.BaseActivity
import com.example.bookapp.core.extensions.replaceFragment
import com.example.bookapp.databinding.ActivityMainBinding
import com.example.bookapp.features.browser.BookBrowserFragment
import com.example.bookapp.features.home.HomeFragment
import com.example.bookapp.features.setting.SettingFragment
import kotlin.jvm.java

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(isFirstCreation()){
            setupBottomNavigation(R.id.navigation_home)
        }
        views.bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    setupBottomNavigation(R.id.navigation_home)
                }
                R.id.navigation_explore -> {
                    setupBottomNavigation(R.id.navigation_explore)
                }
                R.id.navigation_book_list -> {
                    setupBottomNavigation(R.id.navigation_book_list)
                }
                R.id.navigation_setting -> {
                    setupBottomNavigation(R.id.navigation_setting)
                }
                else -> {}
            }
            true
        }
    }

    private fun setupBottomNavigation(id: Int) {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(views.frgCtn.id )

        when (id) {
            R.id.navigation_home -> if (currentFragment !is HomeFragment) {
                replaceFragment(views.frgCtn, HomeFragment::class.java, useCustomAnimation = true)
            }
            R.id.navigation_explore -> if (currentFragment !is BookBrowserFragment) {
                replaceFragment(views.frgCtn, BookBrowserFragment::class.java, useCustomAnimation = true, addToBackStack = true)
            }
            R.id.navigation_setting -> if (currentFragment !is SettingFragment) {
                replaceFragment(views.frgCtn, SettingFragment::class.java, useCustomAnimation = true, addToBackStack = true)
            }
        }
    }
    override fun getBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}