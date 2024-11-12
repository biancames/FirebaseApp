package br.unisanta.appsanta

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import br.unisanta.appsanta.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = Firebase.firestore

        binding.btnSalvar.setOnClickListener {
            val nome = binding.edtNome.text.toString()
            val dia = binding.edtData.dayOfMonth
            val mes = binding.edtData.month + 1
            val ano = binding.edtData.year
            val data = "$dia/$mes/$ano"
            val horario = binding.edtHorario.text.toString()
            val consulta = hashMapOf(
                "nome" to nome,
                "data" to data,
                "horario" to horario
            )
            db.collection("Consultas").add(consulta)
                .addOnSuccessListener {
                    Log.d("SALVAR","Consulta salvou")
                    Toast.makeText(this, "Consulta salvo com sucesso", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.d("SALVAR","Não salvou")
                    Toast.makeText(this, "Deu ruim", Toast.LENGTH_SHORT).show()
                }
        }

        binding.fbBack.setOnClickListener {
            finish()
        }



    }
}