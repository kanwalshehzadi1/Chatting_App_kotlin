package com.example.chattingappkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattingappkotlin.AdapterClass.UserAdapter
import com.example.chattingappkotlin.ModelClass.User
import com.example.chattingappkotlin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {


    //Initialize
    private lateinit var userList : ArrayList<User>
    private lateinit var adapter :  UserAdapter
    private lateinit var mAuth :  FirebaseAuth
    private  lateinit var mDbRef: DatabaseReference



    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        userList = ArrayList()
        adapter = UserAdapter(this, userList)


        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.userRecyclerView.adapter = adapter

        fetchData()
    }

    private fun fetchData(){
        mDbRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()

                for(postSnapshot in snapshot.children){

                    val currentUser = postSnapshot.getValue(User::class.java)
                    if(mAuth.currentUser?.uid != currentUser?.uid) {
                        userList.add(currentUser!!)
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout){
            //write code for logout
            mAuth.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()

            return true
        }
        return true
    }
}