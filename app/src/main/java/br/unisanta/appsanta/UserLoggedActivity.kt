package br.unisanta.appsanta

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.unisanta.appsanta.databinding.ActivityUserloggedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserLoggedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserloggedBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserloggedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userLogged = FirebaseAuth.getInstance().currentUser
        if (userLogged != null) {
            binding.txtNome.text = "Bem vindo, " + userLogged.email.toString() + "!"
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        }

        binding.btnExcluir.setOnClickListener {
            val user = Firebase.auth.currentUser!!
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("EXCLUIR", "DELETADO COM SUCESSO!")
                        Toast.makeText(this, "DELETADO COM SUCESSO!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
        }
    }
}