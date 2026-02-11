package com.example.coderoyale

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coderoyale.Opciones_Login.ActivityLoginEmail
import com.example.coderoyale.databinding.ActivityLoginOptionsBinding
import com.google.firebase.auth.FirebaseAuth

class OpcionesLogin : AppCompatActivity() {

    private lateinit var binding: ActivityLoginOptionsBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion()

        binding.IngresarEmail.setOnClickListener {
            startActivity(Intent(this@OpcionesLogin, ActivityLoginEmail::class.java))
        }
    }

    private fun comprobarSesion(){
        if(firebaseAuth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }


}