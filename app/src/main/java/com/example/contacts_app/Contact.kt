package com.example.contacts_app

data class Contact(
    val name: String,
    val phone: String,
    val email: String,
    val imageRes: Int // Reference to the image resource (e.g., R.drawable.image)
)
