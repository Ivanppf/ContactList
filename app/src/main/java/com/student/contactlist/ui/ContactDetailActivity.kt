package com.student.contactlist.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.student.contactlist.R
import com.student.contactlist.database.DBHelper
import com.student.contactlist.databinding.ActivityContactDetailBinding
import com.student.contactlist.model.Contact

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactDetailBinding
    private lateinit var db: DBHelper
    private lateinit var contact: Contact
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var imageId: Int = -1
    private val REQUEST_PHONE_CALL = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = intent
        val id = i.extras?.getInt("id")
        db = DBHelper(applicationContext)
        if (id != null) {
            contact = db.getContact(id)
            populate()
        } else {
            finish()
        }

        binding.btnEditar.setOnClickListener {
            binding.layoutEditDelete.visibility = View.VISIBLE
            binding.layoutEdit.visibility = View.GONE
            changeEditText(true)
        }

        binding.btnVoltar.setOnClickListener {
            setResult(1, i)
            finish()
        }

        binding.btnSalvar.setOnClickListener {
            val res = db.updateContact(
                id = contact.id,
                name = binding.editName.text.toString(),
                endereco = binding.editEndereco.text.toString(),
                email = binding.editEmail.text.toString(),
                phone = binding.editTelefone.text.toString().toInt(),
                imageId = imageId
            )
            if (res > 0) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.contato_editado),
                    Toast.LENGTH_SHORT
                ).show()
                setResult(1, intent)
                finish()
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.erro_ao_salvar_contato),
                    Toast.LENGTH_SHORT
                ).show()
                setResult(0, intent)
                finish()
            }
        }
        binding.btnCancelar.setOnClickListener {
            binding.layoutEditDelete.visibility = View.GONE
            binding.layoutEdit.visibility = View.VISIBLE
            populate()
            changeEditText(false)
            Toast.makeText(
                applicationContext,
                getString(R.string.operacao_cancelada),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.btnDeletar.setOnClickListener {
            val res = db.deleteContact(contact.id)
            if (res > 0) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.contato_deletado),
                    Toast.LENGTH_SHORT
                ).show()
                setResult(1, intent)
                finish()
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.erro_ao_deletar_contato),
                    Toast.LENGTH_SHORT
                ).show()
                setResult(0, intent)
                finish()
            }
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.resultCode == 1) {
                if (it.data?.extras != null) {
                    imageId = it.data?.getIntExtra("id", -1)!!
                }
            } else {
                imageId = R.drawable.default_profile
            }
            binding.imageContact.setImageResource(imageId)
        }

        binding.imageContact.setOnClickListener {
            if (binding.editName.isEnabled) {
                launcher.launch(Intent(applicationContext, ContactImageActivity::class.java))
            }
        }

        binding.imageEmail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(contact.email))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Enviado por Lista Contatos APP")

            try {
                startActivity(Intent.createChooser(emailIntent, "Choose Email Client..."))
            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        binding.imagePhone.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_PHONE_CALL
                )
            } else {
                val dialIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.telefone))
                startActivity(dialIntent)
            }
        }


    }

    private fun changeEditText(status: Boolean) {
        binding.editName.isEnabled = status
        binding.editEndereco.isEnabled = status
        binding.editEmail.isEnabled = status
        binding.editTelefone.isEnabled = status
    }

    private fun populate() {
        binding.editName.setText(contact.nome)
        binding.editEndereco.setText(contact.endereco)
        binding.editEmail.setText(contact.email)
        binding.editTelefone.setText(contact.telefone.toString())
        if (contact.imageId > 0) {
            imageId = contact.imageId
            binding.imageContact.setImageResource(imageId)
        } else {
            binding.imageContact.setImageResource(R.drawable.default_profile)
            binding.imageContact.drawable.applyTheme(theme)
        }
    }
}