package com.student.contactlist.adapter.listener

import com.student.contactlist.model.Contact

class ContactOnClickListener(val clickListener: (contact: Contact) -> Unit) {
    fun onClick(contact: Contact) = clickListener
}