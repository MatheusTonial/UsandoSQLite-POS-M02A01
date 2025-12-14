package com.tonial.usandosqlite

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tonial.usandosqlite.database.DatabaseHandler
import com.tonial.usandosqlite.databinding.ActivityMainBinding
import com.tonial.usandosqlite.entity.Cadastros
import kotlinx.coroutines.launch

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

        banco = DatabaseHandler.getInstance()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //initView()
    }

    override fun onStart() {
        super.onStart()
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

        lifecycleScope.launch {
            var msg = ""
            if(binding.etCod.text.toString().isEmpty()){
                val cadastro = Cadastros(
                    binding.etCod.text.toString().toInt(),
                    binding.etNome.text.toString(),
                    binding.etNumero.text.toString()
                )
                //acesso ao banco
                banco.create(cadastro)
                msg = "Registro incluido com sucesso"
            }
            else{
                val cadastro = Cadastros(
                    binding.etCod.text.toString().toInt(),
                    binding.etNome.text.toString(),
                    binding.etNumero.text.toString()
                )

                banco.update(cadastro)
                msg = "Registro atualizado com sucesso"
            }
            Toast.makeText(this@MainActivity,msg, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun btnRemoverOnClick(view: View) {

        lifecycleScope.launch {
            banco.delete(binding.etCod.text.toString().toInt())

            //apresentação da devolutiva visual para o usuário
            Toast.makeText(
                this@MainActivity,
                "Exclusão efetuada com Sucesso.",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }

    fun btnBuscarOnClick(view: View) {

        //validação dos campos de tela

        //acesso ao banco
        val etCodPesquisar = EditText(this)

        val builder = AlertDialog.Builder( this )
        builder.setTitle("Digite o Código")
        builder.setView(etCodPesquisar)
        builder.setCancelable(false)
        builder.setNegativeButton(
            "Fechar",
            null
        )

        builder.setPositiveButton(
            "Pesquisar",
            { dialog, which ->

                lifecycleScope.launch {

                    val cadastro = banco.read(etCodPesquisar.text.toString().toInt())

                    if (cadastro != null) {
                        binding.etCod.setText(etCodPesquisar.text.toString())
                        binding.etNome.setText(cadastro.nome)
                        binding.etNumero.setText(cadastro.telefone)
                    } else {
                        binding.etNome.setText("")
                        binding.etNumero.setText("")

                        Toast.makeText(
                            this@MainActivity,
                            "Registro não encontro.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )
        builder.show()
    }

}
