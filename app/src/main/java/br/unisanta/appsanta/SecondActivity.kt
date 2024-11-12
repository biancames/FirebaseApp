package br.unisanta.appsanta

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.unisanta.appsanta.databinding.ActivitySecondBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class SecondActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySecondBinding
    private val produtoList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        val listView: ListView = findViewById(R.id.listView)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, produtoList)
        listView.adapter = adapter
        verConsultas(adapter)
    }

    private fun verConsultas(adapter: ArrayAdapter<String>) {
        val db = Firebase.firestore
        db.collection("Consultas")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nome = document.getString("nome") ?: "Não encontrado"
                    produtoList.add("Nome do Paciente: $nome")
                    val data = document.getString("data") ?: "Não encontrado"
                    produtoList.add("Data: $data")
                    val horario = document.getString("horario") ?: "Não encontrado"
                    produtoList.add("Horario: $horario")

                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                Log.d("RECUPERAR", "Erro ao obter pacientes: ", exception)
                Toast.makeText(this,"Deu ruim", Toast.LENGTH_SHORT).show()
            }

        binding.fbBack.setOnClickListener{
            finish()
        }
    }
}

