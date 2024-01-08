package com.student.contactlist.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.student.contactlist.R
import com.student.contactlist.database.DBHelper
import com.student.contactlist.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = application.getSharedPreferences("Login", Context.MODE_PRIVATE)
        binding.editusername.setText(sp.getString("username", ""))
        binding.editpassword.setText(sp.getString("password", ""))

        binding.btnEntrar.setOnClickListener {
            val username = binding.editusername.text.toString()
            val password = binding.editpassword.text.toString()
            val logged = binding.checkboxLogged.isChecked

            if (username.isNotBlank() && password.isNotBlank()) {
                val db = DBHelper(this)
                if (db.login(username, password)) {
                    val editor = sp.edit()
                    if (logged) {
                        editor.putString("username", username)
                        editor.putString("password", password)
                        editor.apply()
                    } else {
                        editor.clear()
                        editor.apply()
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.erro_de_login),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.editusername.setText("")
                    binding.editpassword.setText("")
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.por_favor_insira_todos_os_campos_requeridos),
                    Toast.LENGTH_SHORT
                ).show()
                if (username.isBlank())
                    binding.editusername.error = ""
                if (password.isBlank())
                    binding.editpassword.error = ""
            }
        }
        binding.textCadastrar.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.textEsqueciSenha.setOnClickListener {}

    }
}