
package com.example.bookapp

import android.content.Intent
import android.os.Bundle
import com.example.bookapp.core.BaseActivity
import com.example.bookapp.core.extensions.replaceFragment
import com.example.bookapp.databinding.ActivityMainBinding
import com.example.bookapp.features.home.HomeFragment
import kotlin.jvm.java

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var prevItemSelected: Int? = null
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
                R.id.navigation_profile -> {
                    setupBottomNavigation(R.id.navigation_profile)
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
                replaceFragment(views.frgCtn, HomeFragment::class.java)
            }
//            R.id.navigation_map -> if (currentFragment !is MapFragment) {
//                replaceFragment(views.frgCtn, MapFragment::class.java)
//            }
//            R.id.navigation_geo_photo -> if (currentFragment !is GeoPhotoFragment) {
//                replaceFragment(views.frgCtn, GeoPhotoFragment::class.java)
//            }
//            R.id.navigation_stream -> {
//                startActivity(Intent(this, StreamActivity::class.java))
//            }
//            R.id.navigation_setting -> if (currentFragment !is SettingFragment) {
//                replaceFragment(views.frgCtn, SettingFragment::class.java)
//            }
        }
    }
    override fun getBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}
