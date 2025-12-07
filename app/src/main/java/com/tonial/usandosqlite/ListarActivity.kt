package com.tonial.usandosqlite

import android.database.Cursor
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tonial.usandosqlite.adapter.MeuAdapter
import com.tonial.usandosqlite.database.DatabaseHandler
import com.tonial.usandosqlite.databinding.ActivityListarBinding

class ListarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListarBinding
    private lateinit var banco: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityListarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler.getInstance(this)

        // Ajusta o padding para as barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initList()
    }

    private fun initList() {
        // Busca os dados do banco
        val cursor: Cursor = banco.readAll()

        // Cria o adapter
        val adapter = MeuAdapter(this, cursor)

        // Configura a RecyclerView
        binding.rvRegistros.apply {
            // Define o layout manager para ser um grid com 2 colunas
            layoutManager = GridLayoutManager(this@ListarActivity, 2)
            // Define o adapter
            this.adapter = adapter
        }
    }
}