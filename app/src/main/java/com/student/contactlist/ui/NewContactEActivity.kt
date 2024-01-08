package com.student.contactlist.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.student.contactlist.R
import com.student.contactlist.database.DBHelper
import com.student.contactlist.databinding.ActivityNewContactEactivityBinding

class NewContactEActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewContactEactivityBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var imageId: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewContactEactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this)
        val intent = Intent()
        binding.btnSalvar.setOnClickListener {
            val name = binding.editName.text.toString()
            val endereco = binding.editEndereco.text.toString()
            val email = binding.editEmail.text.toString()
            val telefone = binding.editTelefone.text.toString().toInt()
            var imageId = R.drawable.default_profile
            if (this.imageId != null) {
                imageId = this.imageId as Int
            }

            if (name.isNotBlank() && endereco.isNotBlank() && email.isNotBlank()) {
                val res = db.insertContact(name, endereco, email, telefone, imageId)
                if (res > 0) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.salvo_com_sucesso),
                        Toast.LENGTH_SHORT
                    ).show()
                    setResult(1, intent)
                    finish()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.erro_ao_salvar_contato),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        binding.btnCancelar.setOnClickListener {
            setResult(0, intent)
            finish()
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.resultCode == 1) {
                imageId = it.data?.extras?.getInt("id")
            } else {
                imageId = R.drawable.default_profile
            }
            binding.imageContact.setImageResource(imageId!!)
        }

        binding.imageContact.setOnClickListener {
            launcher.launch(Intent(applicationContext, ContactImageActivity::class.java))
        }
    }
}