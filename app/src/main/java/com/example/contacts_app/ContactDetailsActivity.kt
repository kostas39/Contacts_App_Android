package com.example.contacts_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat

class ContactDetailsActivity : AppCompatActivity() {

    private val CALL_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)

        // Setup Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable back arrow

        // Close button (X)
        findViewById<ImageView>(R.id.btnClose).setOnClickListener {
            finish() // Close the activity
        }

        // Retrieve contact details from the Intent
        val name = intent.getStringExtra("name")
        val phone = intent.getStringExtra("phone")
        val email = intent.getStringExtra("email")
        val imageRes = intent.getIntExtra("imageRes", 0)

        // Set the data in views
        findViewById<ImageView>(R.id.imageView).setImageResource(imageRes)
        findViewById<TextView>(R.id.tvName).text = name
        findViewById<TextView>(R.id.tvPhone).text = phone
        findViewById<TextView>(R.id.tvEmail).text = email

        // Call button functionality
        findViewById<Button>(R.id.btnCall).setOnClickListener {
            if (phone != null) {
                makePhoneCall(phone)
            } else {
                Toast.makeText(this, "Phone number is not available", Toast.LENGTH_SHORT).show()
            }
        }

        // Message button functionality
        findViewById<Button>(R.id.btnMessage).setOnClickListener {
            if (phone != null) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("smsto:$phone")
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Phone number is not available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makePhoneCall(phone: String) {
        // Check if CALL_PHONE permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Request the CALL_PHONE permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                CALL_REQUEST_CODE
            )
        } else {
            // Permission granted, initiate the call
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phone")
            startActivity(callIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CALL_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val phone = intent.getStringExtra("phone")
                if (phone != null) {
                    makePhoneCall(phone)
                }
            } else {
                Toast.makeText(this, "Permission DENIED to make calls", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Navigate back to the previous screen
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
