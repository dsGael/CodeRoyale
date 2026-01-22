package com.example.coderoyale

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coderoyale.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.BottomNV.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.Item_Inicio->{
                    true
                }
                R.id.Item_Amigos->{
                    true
                }
                R.id.Item_Leaderboard->{
                    true
                }
                R.id. ->{
                    true
                }
                R.id.Item_Cuenta->{
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}