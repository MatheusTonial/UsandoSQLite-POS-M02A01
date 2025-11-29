package com.tonial.usandosqlite.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.tonial.usandosqlite.R
import com.tonial.usandosqlite.database.DatabaseHandler
import com.tonial.usandosqlite.entity.Cadastros

class MeuAdapter(val context: Context, val cursor: Cursor) : BaseAdapter() {

    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(indice: Int): Any? {

        cursor.moveToPosition(indice)

        val cadastros = Cadastros(
            cursor.getInt(DatabaseHandler.COLUMN_ID.toInt()),
            cursor.getString(DatabaseHandler.COLUMN_NOME.toInt()),
            cursor.getString(DatabaseHandler.COLUMN_TELEFONE.toInt())
        )
        return cadastros
    }

    override fun getItemId(indice: Int): Long {
        cursor.moveToPosition(indice)
        return  cursor.getInt(DatabaseHandler.COLUMN_ID.toInt()).toLong()
    }

    override fun getView(
        indice: Int,
        p1: View?,
        p2: ViewGroup?
    ): View? {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.elemento_lista, null)

        val tvNome = view.findViewById<TextView>(R.id.tvElementoLista)
        val tvTelefone = view.findViewById<TextView>(R.id.tvTelefoneElementoLista)

        cursor.moveToPosition(indice)

        tvNome.text = cursor.getString(DatabaseHandler.COLUMN_NOME.toInt())
        tvTelefone.text = cursor.getString(DatabaseHandler.COLUMN_TELEFONE.toInt())

        return view

    }
}