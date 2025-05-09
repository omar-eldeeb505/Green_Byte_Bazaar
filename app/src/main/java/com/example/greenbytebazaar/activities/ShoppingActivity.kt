package com.example.greenbytebazaar.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.greenbytebazaar.R
import com.example.greenbytebazaar.databinding.ActivityShoppingBinding

class ShoppingActivity : AppCompatActivity() {
    val binding by lazy {
       ActivityShoppingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.soppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

    }
}


