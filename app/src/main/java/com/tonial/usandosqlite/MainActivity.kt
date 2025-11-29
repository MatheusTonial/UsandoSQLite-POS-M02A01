package com.tonial.usandosqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tonial.usandosqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var banco: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = openOrCreateDatabase("bdfile.sqlite", MODE_PRIVATE, null)

        banco.execSQL("CREATE TABLE IF NOT EXISTS cadastros(_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT);")


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun btnIncluirOnClick(view: View) {
        val registro = ContentValues()
        registro.put("nome", binding.etNome.text.toString())
        registro.put("telefone", binding.etNumero.text.toString())
        banco.insert("cadastros", null, registro)

        Toast.makeText(this, "Registro incluido com sucesso", Toast.LENGTH_SHORT).show()
        binding.etNome.setText("")
        binding.etNumero.setText("")
        binding.etCod.setText("")
    }

    fun btnEditarOnClick(view: View) {
        val registro = ContentValues()
        registro.put("nome", binding.etNome.text.toString())
        registro.put("telefone", binding.etNumero.text.toString())

        banco.update("cadastros", registro, "_id = ${binding.etCod.text.toString()}", null)

        Toast.makeText(this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show()
        binding.etNome.setText("")
        binding.etNumero.setText("")
        binding.etCod.setText("")
    }

    fun btnRemoverOnClick(view: View) {
        banco.delete("cadastros", "_id = ${binding.etCod.text.toString()}", null)

        Toast.makeText(this, "Registro removido com sucesso", Toast.LENGTH_SHORT).show()
        binding.etNome.setText("")
        binding.etNumero.setText("")
        binding.etCod.setText("")
    }

    fun btnBuscarOnClick(view: View) {

        if(binding.etCod.text.toString() == ""){
            Toast.makeText(this, "Informe o código do registro", Toast.LENGTH_SHORT).show()
            return
        }

       //val cursor: Cursor = banco.rawQuery("SELECT * FROM cadastros WHERE _id = ${binding.etCod.text.toString()}", null)
        val registro: Cursor = banco.query(
            "cadastros",
            null,
            "_id = ${binding.etCod.text.toString()}",
            null, null, null, null)

        if(registro.moveToNext()){
            val nome = registro.getString(1)
            val telefone = registro.getString(2)

            binding.etNome.setText(nome)
            binding.etNumero.setText(telefone)
        }else{
            Toast.makeText(this, "Registro não encontrado", Toast.LENGTH_SHORT).show()
            binding.etNome.setText("")
            binding.etNumero.setText("")
            binding.etCod.setText("")
        }
    }

    fun btnListarOnClick(view: View) {
        val registros = banco.query("cadastros", null, null, null, null, null, null)

        if(registros.count == 0){
            Toast.makeText(this, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val saida = StringBuilder()

        while (registros.moveToNext()){
            val id = registros.getInt(0)
            val nome = registros.getString(1)
            val telefone = registros.getString(2)

            saida.append("ID: $id\n")
            saida.append("Nome: $nome\n")
            saida.append("Telefone: $telefone\n\n")

        }
        Toast.makeText(this, saida.toString(), Toast.LENGTH_SHORT).show()
        binding.etNome.setText("")
        binding.etNumero.setText("")
        binding.etCod.setText("")
    }
}
