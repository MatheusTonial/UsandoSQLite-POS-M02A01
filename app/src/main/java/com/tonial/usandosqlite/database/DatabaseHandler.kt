package com.tonial.usandosqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.tonial.usandosqlite.entity.Cadastros

class DatabaseHandler private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        const val DATABASE_NAME = "bdfile.sqlite"
        const val DATABASE_VERSION = 2
        const val TABLE_NAME = "cadastros"
        const val COLUMN_ID = "_id"
        const val COLUMN_NOME = "nome"
        const val COLUMN_TELEFONE = "telefone"

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
        banco?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NOME TEXT, $COLUMN_TELEFONE TEXT, email TEXT);")
    }

    override fun onUpgrade(
        banco: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        if (oldVersion < 2) {
            banco?.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN email TEXT;")
        }
    }

    fun create(cadastros: Cadastros){
        val registro = ContentValues()
        registro.put(COLUMN_NOME, cadastros.nome)
        registro.put(COLUMN_TELEFONE, cadastros.telefone)
        writableDatabase.insert(TABLE_NAME, null, registro)
    }

    fun update(cadastros: Cadastros){
        val registro = ContentValues()
        registro.put(COLUMN_NOME, cadastros.nome)
        registro.put(COLUMN_TELEFONE, cadastros.telefone)

        writableDatabase.update(TABLE_NAME, registro, "$COLUMN_ID = ?", arrayOf(cadastros._id.toString()))
    }

    fun delete(id: Int){
        writableDatabase.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun read(id: Int): Cadastros? {
        val registro: Cursor = writableDatabase.query(
            TABLE_NAME, null, "$COLUMN_ID = ?", arrayOf(id.toString()), null, null, null
        )

        if(registro.moveToNext()){
            val nome = registro.getString(registro.getColumnIndexOrThrow(COLUMN_NOME))
            val telefone = registro.getString(registro.getColumnIndexOrThrow(COLUMN_TELEFONE))
            return Cadastros(id, nome, telefone)
        }
        registro.close()
        return null
    }

    fun readAll(): Cursor{
        return writableDatabase.query(TABLE_NAME, null, null, null, null, null, null)
    }

    fun search(query: String): Cursor {
        val selection = "$COLUMN_NOME LIKE ? OR $COLUMN_TELEFONE LIKE ?"
        val selectionArgs = arrayOf("%${query}%", "%${query}%")
        return writableDatabase.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
    }
}