package com.example.contacts_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var contactAdapter: ContactAdapter
    private var contacts = mutableListOf<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        // Expand the SearchView by default and set placeholder text
        searchView.isIconified = false
        searchView.queryHint = "Search contacts by name"

        // Load contacts
        contacts = loadContacts()

        // Setup RecyclerView
        contactAdapter = ContactAdapter(contacts) { contact ->
            val intent = Intent(this@MainActivity, ContactDetailsActivity::class.java).apply {
                putExtra("name", contact.name)
                putExtra("phone", contact.phone)
                putExtra("email", contact.email)
                putExtra("imageRes", contact.imageRes)
            }
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = contactAdapter

        // Setup Search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredContacts = contacts.filter {
                    it.name.contains(newText ?: "", ignoreCase = true)
                }
                contactAdapter = ContactAdapter(filteredContacts) { contact ->
                    val intent = Intent(this@MainActivity, ContactDetailsActivity::class.java).apply {
                        putExtra("name", contact.name)
                        putExtra("phone", contact.phone)
                        putExtra("email", contact.email)
                        putExtra("imageRes", contact.imageRes)
                    }
                    startActivity(intent)
                }
                recyclerView.adapter = contactAdapter
                return true
            }
        })
    }

    private fun loadContacts(): MutableList<Contact> {
        val contacts = mutableListOf<Contact>()
        val inputStream = resources.openRawResource(R.raw.sample_contacts)
        val reader = inputStream.bufferedReader()

        reader.forEachLine { line ->
            val parts = line.split(",").map { it.trim().removeSurrounding("\"") }
            if (parts.size == 4) {
                val name = parts[0]
                val email = parts[1]
                val phone = parts[2]
                val imageResName = parts[3]

                val imageRes = resources.getIdentifier(imageResName, "drawable", packageName).takeIf { it != 0 }
                    ?: R.drawable.placeholder

                contacts.add(Contact(name, phone, email, imageRes))
            }
        }

        return contacts
    }
}
