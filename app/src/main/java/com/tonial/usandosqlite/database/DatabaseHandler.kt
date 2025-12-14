package com.tonial.usandosqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import com.tonial.usandosqlite.entity.Cadastros
import kotlinx.coroutines.tasks.await

class DatabaseHandler private constructor(){

    private val firestore = Firebase.firestore

    companion object {
        private const val COLLECTION_NAME = "cadastros"

        @Volatile
        private var INSTANCE: DatabaseHandler? = null

        fun getInstance(): DatabaseHandler {
            return INSTANCE ?: synchronized(this) {
                val instance = DatabaseHandler()
                INSTANCE = instance
                instance
            }
        }
    }


    suspend fun create(cadastros: Cadastros){
        firestore.collection(COLLECTION_NAME)
            .document(cadastros._id.toString())
            .set(cadastros)
            .await()
    }

    suspend fun update(cadastros: Cadastros){
        firestore.collection(COLLECTION_NAME)
            .document(cadastros._id.toString())
            .set(cadastros)
            .await()
    }

    suspend fun delete(id: Int){
        firestore.collection(COLLECTION_NAME)
            .document(id.toString())
            .delete()
            .await()
    }

    suspend fun read(id: Int): Cadastros? {
        val document = firestore.collection(COLLECTION_NAME)
            .document(id.toString())
            .get()
            .await()

        return  document.toObject(Cadastros::class.java)
    }

    suspend fun readAll(): List<Cadastros>{
        return search("")
    }

    suspend fun search(filtro: String): List<Cadastros> {
        val query = firestore.collection(COLLECTION_NAME)
        val snapshot = query.get().await()
        val cadastros = snapshot.toObjects<Cadastros>()

        return  if(filtro.isNotEmpty()) {
            cadastros.filter { it.nome.contains(filtro, ignoreCase = true) }
        }
        else{
            cadastros
        }
    }
}