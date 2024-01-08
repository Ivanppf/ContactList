package com.student.contactlist.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.student.contactlist.R
import com.student.contactlist.databinding.ActivityContactImageBinding

class ContactImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactImageBinding
    private lateinit var i: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        i = intent

        binding.imageProfile1.setOnClickListener { passarID(R.drawable.profile1) }
        binding.imageProfile2.setOnClickListener { passarID(R.drawable.profile2) }
        binding.imageProfile3.setOnClickListener { passarID(R.drawable.profile3) }
        binding.imageProfile4.setOnClickListener { passarID(R.drawable.profile4) }
        binding.imageProfile5.setOnClickListener { passarID(R.drawable.profile5) }
        binding.imageProfile6.setOnClickListener { passarID(R.drawable.profile6) }
        binding.imageProfile7.setOnClickListener { passarID(R.drawable.profile7) }
        binding.imageProfile8.setOnClickListener { passarID(R.drawable.profile8) }
        binding.btnRemoverImagem.setOnClickListener { passarID(R.drawable.default_profile) }

    }

    private fun passarID(profileId: Int) {
        i.putExtra("id", profileId)
        setResult(1, i)
        finish()
    }
}