package com.student.contactlist.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.student.contactlist.model.Contact
import com.student.contactlist.model.User

class DBHelper(context: Context) : SQLiteOpenHelper(context, "database.db", null, 1) {

    val sql = arrayOf(
        "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)",
        "INSERT INTO users (username, password) VALUES ('admin', 'password')",
        "CREATE TABLE contact (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, endereco TEXT, email TEXT, phone INT, imageId INT)",
        "INSERT INTO contact (name, endereco, email, phone, imageId) VALUES ('contato1', 'cidade1', 'contato1@gmail.com', '12345678', '-1')",
        "INSERT INTO contact (name, endereco, email, phone, imageId) VALUES ('contato2', 'cidade2', 'contato2@gmail.com', '98765432', '-1')"
    )

    override fun onCreate(db: SQLiteDatabase) {
        sql.forEach {
            db.execSQL(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    /*---------------------------------------------------------------------------
                                       CRUD USERS
     ---------------------------------------------------------------------------*/
    fun insertUser(username: String, password: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("password", password)
        val res = db.insert("users", null, contentValues)
        db.close()
        return res
    }

    fun updateUser(id: Int, username: String, password: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("password", password)
        val res = db.update("users", contentValues, "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun deleteUser(id: Int): Int {
        val db = this.writableDatabase
        val res = db.delete("users", "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun getUser(username: String, password: String): User {
        val db = this.writableDatabase
        val c = db.rawQuery(
            "SELECT * FROM users WHERE username=? AND password=?",
            arrayOf(username, password)
        )
        var user = User()
        if (c.count == 1) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val usernameIndex = c.getColumnIndex("username")
            val passwordIndex = c.getColumnIndex("password")
            user = User(c.getInt(idIndex), c.getString(usernameIndex), c.getString(passwordIndex))
        }
        db.close()
        return user
    }

    fun login(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val c = db.rawQuery(
            "SELECT * FROM users WHERE username=? AND password=?",
            arrayOf(username, password)
        )
        if (c.count == 1) {
            db.close()
            return true
        } else {
            db.close()
            return false
        }
    }

    /*---------------------------------------------------------------------------
                                      CRUD CONTACTS
    ---------------------------------------------------------------------------*/
    fun insertContact(
        name: String,
        endereco: String,
        email: String,
        phone: Int,
        imageId: Int
    ): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("endereco", endereco)
        contentValues.put("email", email)
        contentValues.put("phone", phone)
        contentValues.put("imageId", imageId)

        val res = db.insert("contact", null, contentValues)
        db.close()
        return res
    }

    fun updateContact(
        id: Int,
        name: String,
        endereco: String,
        email: String,
        phone: Int,
        imageId: Int
    ): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("endereco", endereco)
        contentValues.put("email", email)
        contentValues.put("phone", phone)
        contentValues.put("imageId", imageId)
        val res = db.update("contact", contentValues, "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun deleteContact(id: Int): Int {
        val db = this.writableDatabase
        val res = db.delete("contact", "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun getContact(id: Int): Contact {
        val db = this.writableDatabase
        val c = db.rawQuery(
            "SELECT * FROM contact WHERE id=?",
            arrayOf(id.toString())
        )
        var contact = Contact()
        if (c.count == 1) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val nameIndex = c.getColumnIndex("name")
            val enderecoIndex = c.getColumnIndex("endereco")
            val emailIndex = c.getColumnIndex("email")
            val phoneIndex = c.getColumnIndex("phone")
            val imageIdIndex = c.getColumnIndex("imageId")
            contact = Contact(
                c.getInt(idIndex),
                c.getString(nameIndex),
                c.getString(enderecoIndex),
                c.getString(emailIndex),
                c.getInt(phoneIndex),
                c.getInt(imageIdIndex)
            )
        }
        db.close()
        return contact
    }

    fun getAllContacts(): ArrayList<Contact> {
        val db = this.writableDatabase
        val c = db.rawQuery("SELECT * FROM contact", null)
        val contactList = ArrayList<Contact>()

        if (c.count > 0) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val nameIndex = c.getColumnIndex("name")
            val enderecoIndex = c.getColumnIndex("endereco")
            val emailIndex = c.getColumnIndex("email")
            val phoneIndex = c.getColumnIndex("phone")
            val imageIdIndex = c.getColumnIndex("imageId")
            do {
                val contact = Contact(
                    c.getInt(idIndex),
                    c.getString(nameIndex),
                    c.getString(enderecoIndex),
                    c.getString(emailIndex),
                    c.getInt(phoneIndex),
                    c.getInt(imageIdIndex)
                )
                contactList.add(contact)
            } while (c.moveToNext())
        }
        db.close()
        return contactList
    }


}