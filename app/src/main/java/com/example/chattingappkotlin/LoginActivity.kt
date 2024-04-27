package com.example.chattingappkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.chattingappkotlin.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private val binding : ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    override fun onStart() {
        super.onStart()

        val currentUser : FirebaseUser? = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()

        }
    }    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

//Initialize
        auth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
        binding.btnLogin.setOnClickListener {

            //get text from edit text
            val password:String = binding.etPassword.text.toString()
            val email:String = binding.etEmail.text.toString()

            if (email.isEmpty()){
                binding.etEmail.setError("Required")
            }
            if (password.isEmpty()){
                binding.etPassword.setError("Required")

            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                binding.etEmail.setError("Invalid Email Format")

            }

            else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener (this) { task ->
                        if (task.isSuccessful){
                            Toast.makeText(this," Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }

                        else{
                            Toast.makeText(this,"Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }


            }


        }
    }
}

