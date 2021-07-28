package com.example.test.screen.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.test.R
import com.example.test.databinding.MainScreenBinding
import com.example.test.feature.logging.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.main__screen) {
    @Inject
    lateinit var logger: Logger

    private val binding by viewBinding(MainScreenBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment).navController
        binding.navBar.setupWithNavController(navController)

        // binding.navBar.setOnItemSelectedListener {
        //     NavigationUI.onNavDestinationSelected(it, navController)
        // }
    }
}
