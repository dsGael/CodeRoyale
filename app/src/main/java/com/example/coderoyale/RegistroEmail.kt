package com.example.coderoyale

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coderoyale.databinding.ActivityRegistroEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class RegistroEmail : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: ActivityRegistroEmailBinding

    // Variables para capturar datos
    private var email = ""
    private var password = ""
    private var r_password = ""

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

    private fun validarInfo() {
        email = binding.EtEmail.text.toString().trim()
        password = binding.EtPassword.text.toString().trim()
        r_password = binding.EtRPassword.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.EtEmail.error = "Email inválido"
            binding.EtEmail.requestFocus()
        } else if (email.isEmpty()) {
            binding.EtEmail.error = "Email requerido"
            binding.EtEmail.requestFocus()
        } else if (password.isEmpty()) {
            binding.EtPassword.error = "Contraseña requerida"
            binding.EtPassword.requestFocus()
        } else if (r_password.isEmpty()) {
            binding.EtRPassword.error = "Repetir contraseña requerida"
            binding.EtRPassword.requestFocus()
        } else if (password != r_password) {
            binding.EtRPassword.error = "Contraseñas no coinciden"
            binding.EtPassword.requestFocus()
        } else {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        progressDialog.setMessage("Creando cuenta")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                enviarCorreo(email, password)
                llenarInfoBD()
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "No se registró el usuario debido a ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun enviarCorreo(emailDestino: String, passwordUsuario: String) {
        val correoEmisor = "yorchpds3@gmail.com\n"
        val passwordEmisor = "nkbl yzah tawh nqbm"

        val stringHost = "smtp.gmail.com"
        val properties = Properties()
        properties["mail.smtp.host"] = stringHost
        properties["mail.smtp.socketFactory.port"] = "465"
        properties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.port"] = "465"

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(correoEmisor, passwordEmisor)
            }
        })

        val thread = Thread {
            try {
                val mimeMessage = MimeMessage(session)
                mimeMessage.setFrom(InternetAddress(correoEmisor))
                mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(emailDestino))
                mimeMessage.subject = "Bienvenido a CodeRoyale"

                val textoMensaje = """
                    Hola, bienvenido a CodeRoyale.
                    
                    Tu registro ha sido exitoso.
                    Tu usuario es: $emailDestino
                    Tu contraseña es: $passwordUsuario
                    
                    ¡Gracias por unirte a nuestra comunidad!
                    
                    Atte: El equipo de CodeRoyale
                """.trimIndent()

                mimeMessage.setText(textoMensaje)

                Transport.send(mimeMessage)

                runOnUiThread {
                    Toast.makeText(this, "Correo de bienvenida enviado", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Error al enviar correo: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        thread.start()
    }

    private fun llenarInfoBD() {
        progressDialog.setMessage("Guardando información")

        val tiempo = try {
            Constantes.obtenerTiempoDis()
        } catch (e: Exception) {
            System.currentTimeMillis().toString()
        }

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
                Toast.makeText(
                    this,
                    "No se registró debido a ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}