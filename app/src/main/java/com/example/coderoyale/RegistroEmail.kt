package com.example.coderoyale

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentDialog
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coderoyale.databinding.ActivityRegistroEmailBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.intellij.lang.annotations.Pattern

class RegistroEmail : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private lateinit var binding: ActivityRegistroEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.BtnRegistrar.setOnClickListener {
            validarInfo()
        }
    }

    private var email = ""
    private var password = ""
    private var r_password = ""

    private fun validarInfo() {
        email = binding.EtEmail.text.toString().trim()
        password = binding.EtPassword.text.toString().trim()
        r_password = binding.EtRPassword.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.EtEmail.error = "Email invalido"
            binding.EtEmail.requestFocus()
        }
        else if(email.isEmpty()){
            binding.EtEmail.error = "Email requerido"
            binding.EtEmail.requestFocus()
        }
        else if(password.isEmpty()){
            binding.EtPassword.error = "Contraseña requerida"
            binding.EtPassword.requestFocus()
        }
        else if(r_password.isEmpty()){
            binding.EtRPassword.error = "Repetir contraseña requerida"
            binding.EtRPassword.requestFocus()
        }
        else if(password != r_password){
            binding.EtRPassword.error = "Contraseñas no coinciden"
            binding.EtPassword.requestFocus()
        }
        else {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        progressDialog.setMessage("Creando cuenta")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                llenarInfoBD()
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                Toast.makeText(this,"No se registró el usuario debido a ${exception.message}", Toast.LENGTH_SHORT).show()

            }
    }

    private fun llenarInfoBD() {
        progressDialog.setMessage("Guardando información")

        val tiempo = Constantes.obtenerTiempoDis()
        val emailUsuario = firebaseAuth.currentUser!!.email
        val uidUsuario = firebaseAuth.uid

        val hashMap = HashMap<String, Any>()
        hashMap["nombres"] = ""
        hashMap["codigoTelefono"] = ""
        hashMap["telefono"] = ""
        hashMap["urlImagenPerfil"] = ""
        hashMap["provedor"] = "email"
        hashMap["escribiendo"] = ""
        hashMap["tiempo"] = tiempo
        hashMap["online"] = true
        hashMap["email"] = "${emailUsuario}"
        hashMap["uid"] = "${uidUsuario}"
        hashMap["fecha_nac"] = ""

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(uidUsuario!!).setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                Toast.makeText(this, "No se registró debido a ${exception.message}", Toast.LENGTH_SHORT).show()

            }
    }
}