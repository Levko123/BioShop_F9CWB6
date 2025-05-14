package com.example.bioshop.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.bioshop.R                 // ← EZ kell az unresolved R-hez
import com.example.bioshop.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NavController lekérése Activity-re szabott extensionnel
        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(binding.bottomNav, navController)
    }
}
