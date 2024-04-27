package com.example.chattingappkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.chattingappkotlin.ModelClass.User
import com.example.chattingappkotlin.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabaseKtxRegistrar

class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private  lateinit var auth: FirebaseAuth
    private  lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        //initialize firebase auth

        auth = FirebaseAuth.getInstance()
        binding.tvlogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        binding.btnSignUp.setOnClickListener {

            //get text from edit text
            val email :String = binding.etEmail.text.toString()
            val name :String = binding.etName.text.toString()
            val password :String = binding.etPassword.text.toString()


            //check if any feild is blank
            if (email.isEmpty()){
                binding.etEmail.setError("Required")
            }
            if (password.isEmpty()){
                binding.etPassword.setError("Required")

            }
            if (name.isEmpty()){
                binding.etName.setError("Required")
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                binding.etEmail.setError("Invalid Email Format")

            }

            else{
                auth. createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful){
                            //code from jumping to home
                            addUserTodatabase(name,email,auth.currentUser?.uid!!)
                            Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this,"Registeration Failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT).show()
                        }

                    }
            }




        }
    }
    private  fun addUserTodatabase(name: String, email: String, uid: String ){
      mDbRef=  FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(User(name,email,uid))




    }



}