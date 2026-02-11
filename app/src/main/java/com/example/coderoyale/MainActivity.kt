package com.example.coderoyale

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coderoyale.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.coderoyale.Fragmentos.FragmentInicio
import com.example.coderoyale.Fragmentos.FragmentCuenta
import com.example.coderoyale.Fragmentos.FragmentAmigos
import com.example.coderoyale.Fragmentos.FragmentEjercicios
import com.example.coderoyale.Fragmentos.FragmentLeaderboard


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion()

        verFragmentInicio()

        binding.BottomNV.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Item_Inicio -> {
                    verFragmentInicio()
                    true
                }

                R.id.Item_Amigos -> {
                    verFragmentAmigos()
                    true
                }

                R.id.Item_Leaderboard -> {
                    verFragmentLeaderboard()
                    true
                }

                R.id.Item_Ejercicios -> {
                    verFragmentEjercicios()
                    true
                }

                R.id.Item_Cuenta -> {
                    verFragmentCuenta()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun comprobarSesion() {
        if (firebaseAuth.currentUser == null) {
            startActivity(Intent(this, OpcionesLogin::class.java))
            finishAffinity()
        }
    }

    private fun verFragmentInicio() {
        binding.TituloRL.text = "Inicio"
        val fragment = FragmentInicio()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentInicio")
        fragmentTransition.commit()
    }

    private fun verFragmentAmigos() {
        binding.TituloRL.text = "Amigos"
        val fragment = FragmentAmigos()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentAmigos")
        fragmentTransition.commit()
    }

    private fun verFragmentLeaderboard() {
        binding.TituloRL.text = "Leaderboard"
        val fragment = FragmentLeaderboard()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentLeaderboard")
        fragmentTransition.commit()
    }

    private fun verFragmentEjercicios() {
        binding.TituloRL.text = "Ejercicios"
        val fragment = FragmentEjercicios()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentEjercicios")
        fragmentTransition.commit()
    }

    private fun verFragmentCuenta() {
        binding.TituloRL.text = "Cuenta"
        val fragment = FragmentCuenta()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentCuenta")
        fragmentTransition.commit()
    }



}