package com.tonial.usandosqlite

import android.database.Cursor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SimpleCursorAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lvRegistros)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initList()
    }

    private fun initList() {
//        val lista = listOf<String>("Brasil", "Argentina", "Chile", "Uruguai")
//
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lista)
//
//        binding.lvRegistros.adapter = adapter

        val cursor: Cursor = banco.readAll()

//        val adapter = SimpleCursorAdapter(
//            this,
//            android.R.layout.simple_list_item_2,
//            cursor,
//            arrayOf<String>("nome", "telefone"),
//            intArrayOf(android.R.id.text1, android.R.id.text2),
//            0
//        )
        val adapter = MeuAdapter(this, cursor)

        binding.lvRegistros.adapter = adapter



    }
}