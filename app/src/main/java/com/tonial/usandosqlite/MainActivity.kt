package com.tonial.usandosqlite

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tonial.usandosqlite.database.DatabaseHandler
import com.tonial.usandosqlite.databinding.ActivityMainBinding
import com.tonial.usandosqlite.entity.Cadastros

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var banco: DatabaseHandler
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //banco = openOrCreateDatabase("bdfile.sqlite", MODE_PRIVATE, null)
        //banco.execSQL("CREATE TABLE IF NOT EXISTS cadastros(_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT);")

        banco = DatabaseHandler.getInstance(this)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
    }

    private fun initView() {
        if(intent.getIntExtra("id", 0) != 0){
            binding.etCod.setText(intent.getIntExtra("id", 0).toString())
            binding.etNome.setText(intent.getStringExtra("nome"))
            binding.etNumero.setText(intent.getStringExtra("telefone"))
        }
        else{
            binding.btnExcluir.visibility = View.GONE
            binding.btnBuscar.visibility = View.GONE
        }
    }

    fun btnSalvarOnClick(view: View) {
        var msg = ""

        val cadastro = Cadastros(
            _id = binding.etCod.text.toString().toInt(),
            nome = binding.etNome.text.toString(),
            telefone = binding.etNumero.text.toString()
        )

        db.collection("cadastros")
            .document(binding.etCod.text.toString())
            .set(cadastro)
            .addOnSuccessListener {
                msg = "Cadastro realizado com sucesso!"
                //Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                msg = "Erro ao realizar o cadastro: ${e.message}"
                //Toast.makeText(this, "Erro ao realizar o cadastro: ${e.message}", Toast.LENGTH_SHORT).show()
            }

//        if(binding.etCod.text.toString().isEmpty()){
//            //incluir
//            banco.create(Cadastros(0, binding.etNome.text.toString(), binding.etNumero.text.toString()))
//            msg = "Registro incluido com sucesso"
//        }
//        else{
//            //editar
//            banco.update(Cadastros(binding.etCod.text.toString().toInt(), binding.etNome.text.toString(), binding.etNumero.text.toString()))
//            msg = "Registro atualizado com sucesso"
//        }

        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }

    fun btnRemoverOnClick(view: View) {

        banco.delete(binding.etCod.text.toString().toInt())

        Toast.makeText(this, "Registro removido com sucesso", Toast.LENGTH_SHORT).show()
        binding.etNome.setText("")
        binding.etNumero.setText("")
        binding.etCod.setText("")
    }

    fun btnBuscarOnClick(view: View) {

        val msg = StringBuilder()

        db.collection("cadastros")
            //.document(binding.etCod.text.toString())//where
            .get()
            .addOnSuccessListener { result ->
                val resgistro = result.toString()
                for (document in result) {
                    msg.append(document.getString("nome"))
                    msg.append("\n")

                }
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }

//        if(binding.etCod.text.toString() == ""){
//            Toast.makeText(this, "Informe o código do registro", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val etCodPesquisa = EditText(this)
//
//
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Digite o codigo")
//        //builder.setMessage("Deseja pesquisar o registro?")
//        builder.setView(etCodPesquisa)
//        builder.setCancelable(false)
//        builder.setNegativeButton(
//            "Ficarechar",
//            null)
//        builder.setPositiveButton(
//            "Pesquisar",
//            { dialog, which ->
//                        val registro: Cadastros? = banco.read(etCodPesquisa.text.toString().toInt())
//                        if(registro != null){
//                            val nome = registro.nome
//                            val telefone = registro.telefone
//
//                            binding.etCod.setText(etCodPesquisa.text.toString())
//                            binding.etNome.setText(nome)
//                            binding.etNumero.setText(telefone)
//
//                        }else{
//                            Toast.makeText(this, "Registro não encontrado", Toast.LENGTH_SHORT).show()
//                            binding.etNome.setText("")
//                            binding.etNumero.setText("")
//                            binding.etCod.setText("")
//                        }
//            })
//        builder.show()



    }

//    fun btnListarOnClick(view: View) {
//        val intent = Intent(this, ListarActivity::class.java)
//        startActivity(intent)
//
////        val registros = banco.readAll()
////
////        if(registros.count == 0){
////            Toast.makeText(this, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show()
////            return
////        }
////
////        val saida = StringBuilder()
////
////        var index: Int = 0;
////        while (registros.moveToNext()){
////            if(index > 0 ){
////                saida.append("\n\n")
////            }
////            val nome = registros.getString(DatabaseHandler.COLUMN_NOME.toInt())
////            val telefone = registros.getString(DatabaseHandler.COLUMN_TELEFONE.toInt())
////
////            saida.append("Nome: $nome\n")
////            saida.append("Telefone: $telefone")
////
////            index++;
////        }
////        Toast.makeText(this, saida.toString(), Toast.LENGTH_SHORT).show()
////        binding.etNome.setText("")
////        binding.etNumero.setText("")
////        binding.etCod.setText("")
//    }
}
