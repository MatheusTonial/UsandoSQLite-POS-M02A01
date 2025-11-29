package com.tonial.usandosqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.tonial.usandosqlite.entity.Cadastros

// A versão do banco foi incrementada para 2 para acionar o onUpgrade.
class DatabaseHandler private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        const val DATABASE_NAME = "bdfile.sqlite"
        const val DATABASE_VERSION = 2
        const val TABLE_NAME = "cadastros"
        const val COLUMN_ID = ""
        const val COLUMN_NOME = ""
        const val COLUMN_TELEFONE = ""

        @Volatile
        private var INSTANCE: DatabaseHandler? = null
        fun getInstance(context: Context): DatabaseHandler {
            if (INSTANCE == null) {
                INSTANCE = DatabaseHandler(context.applicationContext)
            }
            return INSTANCE as DatabaseHandler
        }
    }

    override fun onCreate(banco: SQLiteDatabase?) {
        banco?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME(_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT, email TEXT);")
    }

    override fun onUpgrade(
        banco: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        // Use uma estrutura de if para aplicar as alterações de forma incremental
        // a cada nova versão do banco de dados.
        if (oldVersion < 2) {
            // Exemplo: Adicionando uma coluna 'email' na tabela 'cadastros'.
            // Substitua com o ALTER TABLE que você precisa.
            banco?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN email TEXT;")
        }
        // if (oldVersion < 3) {
        //     banco?.execSQL("ALTER TABLE ...")
        // }
    }

    fun create(cadastros: Cadastros){
        val registro = ContentValues()
        registro.put("nome", cadastros.nome)
        registro.put("telefone", cadastros.telefone)
        writableDatabase.insert(TABLE_NAME, null, registro)
    }

    fun update(cadastros: Cadastros){
        val registro = ContentValues()
        registro.put("nome", cadastros.nome)
        registro.put("telefone", cadastros.telefone)

        writableDatabase.update(TABLE_NAME, registro, "_id = ${cadastros._id}", null)
    }

    fun delete(id: Int){
        writableDatabase.delete(TABLE_NAME, "_id = ${id}", null)
    }

    fun read(id: Int): Cadastros? {
        val registro: Cursor = writableDatabase.query(
            TABLE_NAME,
            null,
            "_id = ${id}",
            null, null, null, null)

        if(registro.moveToNext()){
            val nome = registro.getString(1)
            val telefone = registro.getString(2)

            return Cadastros(id, nome, telefone)
        }
        return null
    }

    fun readAll(): Cursor{

        val registros = writableDatabase.query(TABLE_NAME, null, null, null, null, null, null)
        return registros

    }

}