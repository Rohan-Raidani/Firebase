package com.example.firebase

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    val database:FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRefrence : DatabaseReference = database.reference.child("MyUsers")
    val userList = ArrayList<Users>()
    lateinit var usersAdapter: UsersAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        val view = mainBinding.root
        setContentView(view)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        mainBinding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = usersAdapter.getUserId(viewHolder.adapterPosition)

                myRefrence.child(id).removeValue()
                Toast.makeText(applicationContext,"User Deleted",Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(mainBinding.recyclerView)


        retrieveDataFromDatabase()

    }
    fun retrieveDataFromDatabase(){
        myRefrence.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (eachUser in snapshot.children){
                    val user = eachUser.getValue(Users::class.java)
                if(user != null){
                    println("UserId = ${user.userId}")
                    println("UserName = ${user.userName}")
                    println("UserAge = ${user.userAge}")
                    println("UserEmail = ${user.userEmail}")
                    println("*****************************")

                    userList.add(user)
                }
                    usersAdapter = UsersAdapter(this@MainActivity,userList)
                    mainBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    mainBinding.recyclerView.adapter = usersAdapter

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_all,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.deleteAll){
            showDialogMessage()
        }

        return super.onOptionsItemSelected(item)
    }
    fun showDialogMessage(){
        val dialogMessage = AlertDialog.Builder(this)
        dialogMessage.setTitle("Delete All Users")
        dialogMessage.setMessage("If you click yes all the users will be deleted" + "To delete specific user swipe the item right or left")
        dialogMessage.setNegativeButton("Cancel",DialogInterface.OnClickListener{ dialogInterface, i->
        dialogInterface.cancel()})
        dialogMessage.setPositiveButton("Yes" , DialogInterface.OnClickListener{ dialogInterface, i->
            myRefrence.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful){
                    usersAdapter.notifyDataSetChanged()
                    Toast.makeText(applicationContext,"All users were deleted",Toast.LENGTH_SHORT).show()
                }
            }})
        dialogMessage.create().show()
    }
}