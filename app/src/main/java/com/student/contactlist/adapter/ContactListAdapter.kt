package com.student.contactlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.student.contactlist.R
import com.student.contactlist.adapter.listener.ContactOnClickListener
import com.student.contactlist.adapter.viewHolder.ContactViewHolder
import com.student.contactlist.model.Contact

class ContactListAdapter(
    private val contactList: List<Contact>,
    private val contactOnClickListener: ContactOnClickListener
) : RecyclerView.Adapter<ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.textName.text = contact.nome
        if (contact.imageId > 0) {
            holder.image.setImageResource(contact.imageId)
        } else {
            holder.image.setImageResource(R.drawable.default_profile)

        }
        holder.itemView.setOnClickListener {
            contactOnClickListener.clickListener(contact)
        }
    }
}