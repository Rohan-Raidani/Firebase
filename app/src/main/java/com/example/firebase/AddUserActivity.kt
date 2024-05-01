package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityAddUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddUserActivity : AppCompatActivity() {

    lateinit var addUserBinding: ActivityAddUserBinding
    val database :FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRefrence :DatabaseReference = database.reference.child("MyUsers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addUserBinding = ActivityAddUserBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        val view = addUserBinding.root
        setContentView(view)
        supportActionBar?.title = "Add User"
        addUserBinding.buttonAddUser.setOnClickListener { addUserToDatabase()
            addUserBinding.editTextName.text.clear()
            addUserBinding.editTextAge.text.clear()
            addUserBinding.editTextEmail.text.clear()

        val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


    }

    fun addUserToDatabase() {
        val name :String = addUserBinding.editTextName.text.toString()
        val age :Int = addUserBinding.editTextAge.text.toString().toInt()
        val email :String = addUserBinding.editTextEmail.text.toString()

        val id :String = myRefrence.push().key.toString()
        val user = Users(id,name,age,email)

        myRefrence.child(id).setValue(user).addOnCompleteListener {task ->
            if (task.isSuccessful){
            Toast.makeText(applicationContext,"User Added",Toast.LENGTH_SHORT).show()}
            else{Toast.makeText(applicationContext,task.exception.toString(),Toast.LENGTH_SHORT).show()}
        }
    }
}