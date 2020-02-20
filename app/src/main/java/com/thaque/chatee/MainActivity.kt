package com.thaque.chatee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = FirebaseDatabase.getInstance().reference
       // val myRef = database.getReference("message")

        database.setValue("Hello World")

        login_button.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
