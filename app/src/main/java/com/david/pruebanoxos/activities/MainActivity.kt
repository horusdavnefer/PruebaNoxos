package com.david.pruebanoxos.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.david.pruebanoxos.activities.viewmodels.MainViewModel
import com.david.pruebanoxos.databinding.LandingHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: LandingHomeBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LandingHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}