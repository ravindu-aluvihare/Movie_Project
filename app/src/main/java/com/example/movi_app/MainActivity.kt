package com.example.movi_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment // Import NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment?.navController

        navController?.let {
            setupActionBarWithNavController(it)

            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

            // Listen for destination changes
            it.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.movieDetailFragment) {
                    bottomNavigationView.visibility = View.GONE
                } else {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        } ?: run {
            Log.e(TAG, "Failed to set up action bar: NavController is null")
        }

        // Setup BottomNavigationView with NavController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        if (bottomNavigationView != null && navController != null) {
            bottomNavigationView.setupWithNavController(navController)
        } else {
            Log.e(TAG, "BottomNavigationView or NavController is null. Cannot set up bottom navigation.")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment?.navController

        return navController?.navigateUp() ?: super.onSupportNavigateUp()
    }
}
