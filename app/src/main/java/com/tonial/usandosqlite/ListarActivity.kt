package com.tonial.usandosqlite

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tonial.usandosqlite.adapter.MeuAdapter
import com.tonial.usandosqlite.database.DatabaseHandler
import com.tonial.usandosqlite.databinding.ActivityListarBinding

class ListarActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityListarBinding
    private lateinit var banco: DatabaseHandler
    private var adapter: MeuAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityListarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        banco = DatabaseHandler.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.fabAdicionar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Configura a RecyclerView uma vez
        binding.rvRegistros.layoutManager = GridLayoutManager(this, 2)
    }

    override fun onResume() {
        super.onResume()
        // Carrega todos os dados quando a tela volta ao foco
        updateList(banco.readAll())
    }

    private fun updateList(cursor: Cursor) {
        adapter = MeuAdapter(this, cursor)
        binding.rvRegistros.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        // Opcional: Ação ao submeter a busca (pressionar enter)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        // Ação ao digitar na barra de busca
        val cursor = if (newText.isNullOrBlank()) {
            banco.readAll()
        } else {
            banco.search(newText)
        }
        updateList(cursor)
        return true
    }
}