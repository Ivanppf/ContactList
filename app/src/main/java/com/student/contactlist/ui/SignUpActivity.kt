package com.student.contactlist.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.student.contactlist.R
import com.student.contactlist.database.DBHelper
import com.student.contactlist.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this)

        binding.btnCadastrar.setOnClickListener {
            val username = binding.editusername.text.toString().trim()
            val password = binding.editpassword.text.toString().trim()
            val confirmPassword = binding.editconfirmpassword.text.toString().trim()
            if (username.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()) {
                if (password.equals(confirmPassword)) {
                    val res = db.insertUser(username, password)
                    if (res > 0) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.cadastro_concluido),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.erro_de_cadastro),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.editusername.setText("")
                        binding.editpassword.setText("")
                        binding.editconfirmpassword.setText("")

                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.as_senhas_devem_ser_iguais),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.editpassword.error = ""
                    binding.editconfirmpassword.error = ""
                }
            } else {
                if (username.isBlank())
                    binding.editusername.error = ""
                if (password.isBlank())
                    binding.editpassword.error = ""
                if (confirmPassword.isBlank())
                    binding.editconfirmpassword.error = ""

                Toast.makeText(
                    applicationContext,
                    getString(R.string.por_favor_insira_todos_os_campos_requeridos),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}