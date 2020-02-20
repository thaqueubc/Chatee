package com.thaque.chatee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity: AppCompatActivity() {
    private lateinit var messagesDB: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)
        messagesDB = FirebaseDatabase.getInstance().getReference("Messages")
        send_message_button.setOnClickListener {
            val sender = FirebaseAuth.getInstance().currentUser?.email
            val message = message_input.text.toString()

            if (sender != null) {
                saveMessage(sender, message)
            }
        }
    }

    private fun saveMessage(sender: String, messageBody: String) {
        val key = messagesDB.push().key
        key ?: return

        val message = Message(sender, messageBody)

        messagesDB.child(key).setValue(message)
    }
}
data class Message(
    var sender: String = "",
    var messageBody: String = "")