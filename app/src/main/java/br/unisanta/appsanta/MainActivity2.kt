package br.unisanta.appsanta

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.unisanta.appsanta.databinding.ActivityMain2Binding
import br.unisanta.appsanta.databinding.ActivitySecondBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class MainActivity2 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMain2Binding
    var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth


        binding.btnLogin.setOnClickListener {
            fazerLogin(binding.edtEmail.text.toString(), binding.edtSenha.text.toString())
        }

        binding.btnCadastrar.setOnClickListener {
            auth.createUserWithEmailAndPassword(binding.edtEmail.text.toString(),binding.edtSenha.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("CADASTRO", "CADASTRADO COM SUCESSO!")
                        Toast.makeText(this, "CADASTRADO COM SUCESSO!", Toast.LENGTH_SHORT).show()
                        val email = binding.edtEmail.text.toString()
                        val senha = binding.edtSenha.text.toString()
                        val isMedico = binding.checkMedico.isChecked
                        val usuario = hashMapOf(
                            "email" to email,
                            "senha" to senha,
                            "isMedico" to isMedico
                        )
                        db.collection("Usuarios").add(usuario)
                            .addOnSuccessListener {
                                Log.d("Cadastrou","cadastrou no firestore")
                            }
                    } else {
                        Log.d("CADASTRO", "CADASTRO FALHOU")
                        Toast.makeText(this, "CADASTRO FALHOU", Toast.LENGTH_SHORT).show()
                    }
                }
        }


        binding.btnEsqueci.setOnClickListener {
            auth.sendPasswordResetEmail(binding.edtEmail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("ENVIADO", "EMAIL DE REDEFINIÇÃO ENVIADO!")
                        Toast.makeText(this, "EMAIL DE REDEFINIÇÃO ENVIADO!", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("ENVIADO", "EMAIL DE REDEFINIÇÃO FALHOU!")
                        Toast.makeText(this, "DEU RUIM NO EMAIL", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    fun fazerLogin(email:String, senha:String){
        auth.signInWithEmailAndPassword(email,senha)
            .addOnCompleteListener(this) {
                    task -> if(task.isSuccessful){
                Log.d("LOGIN", "SUCESSO!")
                Toast.makeText(this,"SUCESSO", Toast.LENGTH_SHORT).show()

                db.collection("Usuarios")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val emailComp = document.getString("email")
                            if(emailComp == email){
                                val isMedico = document.getBoolean("isMedico")
                                if(isMedico == true){
                                    val intent = Intent(this, SecondActivity::class.java)
                                    startActivity(intent)
                                }
                                else{
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }}


            } else {
                Log.d("LOGIN", "FALHOU")
                Toast.makeText(this,"Deu ruim", Toast.LENGTH_SHORT).show()                }
            }
        binding.edtEmail.text.clear()
        binding.edtSenha.text.clear()
    }
}