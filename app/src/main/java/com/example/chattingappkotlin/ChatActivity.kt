package com.example.chattingappkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattingappkotlin.AdapterClass.MessageAdapter
import com.example.chattingappkotlin.ModelClass.Message
import com.example.chattingappkotlin.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList : ArrayList<Message>
    private lateinit var mDbRef : DatabaseReference

    var receiverRoom : String? = null
    var senderRoom : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mDbRef = FirebaseDatabase.getInstance().getReference()

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        val name = intent.getStringExtra("name")
        val receiverUid= intent.getStringExtra("uid")

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title= name

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = messageAdapter

        // Fetch data on Recycler
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for(postSnapshot in snapshot.children){

                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
            )


        // To add message to database
        binding.sendBtn.setOnClickListener {


            val message = binding.messagebox.text.toString()
            val messageObject = Message(message,senderUid)

            if (message.trim().isNotEmpty()){
            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject)

                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)

                }
            else{
                 Toast.makeText(this," Please type something",Toast.LENGTH_SHORT).show()
            }



            binding.messagebox.text = null



        }
        }





    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }


}