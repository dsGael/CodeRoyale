package com.example.coderoyale.Fragmentos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coderoyale.OpcionesLogin
import com.example.coderoyale.R
import com.example.coderoyale.databinding.FragmentCuentaBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.random.Random


class FragmentCuenta : Fragment() {
    private lateinit var binding: FragmentCuentaBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCuentaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val numeroAleatorio = Random.nextInt(1, 7)
        val nombreImagen="perfil_$numeroAleatorio"
        val idImagen = resources.getIdentifier(nombreImagen, "drawable", mContext.packageName)
        if (idImagen != 0) {
            binding.IvPerfil.setImageResource(idImagen)
        } else {
            binding.IvPerfil.setImageResource(R.drawable.login)
        }


        firebaseAuth = FirebaseAuth.getInstance()
        binding.BtnLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(mContext, OpcionesLogin::class.java))
            activity?.finishAffinity()
        }
    }
}