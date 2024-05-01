package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebase.databinding.ActivityUpdateUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateUserActivity : AppCompatActivity() {
    lateinit var updateUserBinding: ActivityUpdateUserBinding
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRefrence : DatabaseReference = database.reference.child("MyUsers")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateUserBinding = ActivityUpdateUserBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        val view = updateUserBinding.root
        setContentView(view)
        supportActionBar?.title = "Update User"

        getAndSetData()
        updateUserBinding.buttonUpdateUser.setOnClickListener {
            changeData()
        }

    }
    fun getAndSetData(){
        val name = intent.getStringExtra("name")
        val age = intent.getIntExtra("age",0).toString()
        val email = intent.getStringExtra("email")

        updateUserBinding.editTextUpdateName.setText(name)
        updateUserBinding.editTextUpdateAge.setText(age)
        updateUserBinding.editTextUpdateEmail.setText(email)
    }
    fun changeData(){
        val userId = intent.getStringExtra("id").toString()
        val name :String = updateUserBinding.editTextUpdateName.text.toString()
        val age :Int = updateUserBinding.editTextUpdateAge.text.toString().toInt()
        val email :String = updateUserBinding.editTextUpdateEmail.text.toString()

        val userMap = mutableMapOf<String,Any>()
        userMap["userId"] = userId
        userMap["userName"] = name
        userMap["userAge"] = age
        userMap["userEmail"] = email

        myRefrence.child(userId).updateChildren(userMap).addOnCompleteListener {task ->
            if (task.isSuccessful){
                Toast.makeText(this,"User Updated" , Toast.LENGTH_SHORT).show()
                finish()
            }
        }





    }
}