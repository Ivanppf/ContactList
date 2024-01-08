package com.student.contactlist.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.contactlist.R
import com.student.contactlist.adapter.ContactListAdapter
import com.student.contactlist.adapter.listener.ContactOnClickListener
import com.student.contactlist.database.DBHelper
import com.student.contactlist.databinding.ActivityMainBinding
import com.student.contactlist.model.Contact

class MainActivity : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private lateinit var contactList: List<Contact>
    private lateinit var adapter: ContactListAdapter
    private lateinit var db: DBHelper
    private lateinit var result: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityMainBinding
    private var ascDesc = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DBHelper(this)
        sp = application.getSharedPreferences("Login", Context.MODE_PRIVATE)
        binding.btnSair.setOnClickListener {
            val editor = sp.edit()
            editor.clear()
            editor.apply()
            finish()
        }

        binding.recyclerviewContacts.layoutManager = LinearLayoutManager(applicationContext)
        popularLista()

        binding.btnAdd.setOnClickListener {
            result.launch(Intent(this, NewContactEActivity::class.java))
        }
        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null && it.resultCode == 1) {
                popularLista()
            }
//                        else if (it.data != null && it.resultCode == 0) {
//            }
        }

        binding.btnOrder.setOnClickListener {
            if (ascDesc) {
                binding.btnOrder.setImageResource(R.drawable.baseline_arrow_upward_24)
            } else {
                binding.btnOrder.setImageResource(R.drawable.baseline_arrow_downward_24)
            }
            ascDesc = !ascDesc
            contactList = contactList.reversed()
            placeAdapter()
        }

    }

    private fun popularLista() {
        contactList = db.getAllContacts().sortedWith(compareBy { it.nome })
        placeAdapter()

    }

    private fun placeAdapter() {
        adapter = ContactListAdapter(contactList, ContactOnClickListener {
            val intent = Intent(this, ContactDetailActivity::class.java)
            intent.putExtra("id", it.id)
            result.launch(intent)
        })
        binding.recyclerviewContacts.adapter = adapter
    }
}