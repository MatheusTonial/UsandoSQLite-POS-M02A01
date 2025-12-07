package com.tonial.usandosqlite.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tonial.usandosqlite.R
import com.tonial.usandosqlite.database.DatabaseHandler

class MeuAdapter(
    private val context: Context,
    private val cursor: Cursor
) : RecyclerView.Adapter<MeuAdapter.ViewHolder>() {

    // Mantém as referências para as views de cada item do layout (elemento_lista.xml)
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNome: TextView = view.findViewById(R.id.tvElementoLista)
        val tvTelefone: TextView = view.findViewById(R.id.tvTelefoneElementoLista)
        val btEditar: ImageView = view.findViewById(R.id.btEditarElementoLista)
    }

    // Cria uma nova view (invocado pelo layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Infla o layout do item (elemento_lista.xml) para criar a view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.elemento_lista, parent, false)
        return ViewHolder(view)
    }

    // Retorna o número total de itens na lista
    override fun getItemCount(): Int {
        return cursor.count
    }

    // Substitui o conteúdo de uma view (invocado pelo layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Move o cursor para a posição correta
        if (cursor.moveToPosition(position)) {
            // Extrai os dados do cursor
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
            val nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"))
            val telefone = cursor.getString(cursor.getColumnIndexOrThrow("telefone"))

            // Define o texto nas TextViews do ViewHolder
            holder.tvNome.text = nome
            holder.tvTelefone.text = telefone

            // Exemplo de como adicionar um evento de clique no botão de editar
            holder.btEditar.setOnClickListener {
                Toast.makeText(context, "Editando: $nome (ID: $id)", Toast.LENGTH_SHORT).show()
                // Aqui você pode iniciar uma nova activity para edição, por exemplo
            }
        }
    }
}
