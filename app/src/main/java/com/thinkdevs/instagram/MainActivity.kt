package com.thinkdevs.instagram

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	
	var mAuth:FirebaseAuth? = null
	var mAuthlistener :FirebaseAuth.AuthStateListener?=null
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		mAuth = FirebaseAuth.getInstance()
		mAuthlistener = FirebaseAuth.AuthStateListener{}
	}
	
	fun signup(view:View){
		mAuth!!.createUserWithEmailAndPassword(username.text.toString(), password.text.toString())
				.addOnCompleteListener { task ->
					if (task.isSuccessful){
						val intent = Intent(applicationContext, FeedActivity::class.java)
						startActivity(intent)
						Toast.makeText(applicationContext, "User Created ", Toast.LENGTH_LONG).show()
						
					}
					
				}.addOnFailureListener { exception ->
					if (exception != null){
						Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
						
					}
				}
	
	}
	
	fun sign(view:View){
	
		mAuth!!.signInWithEmailAndPassword(username.text.toString(), password.text.toString())
				.addOnCompleteListener {task ->
					if (task.isSuccessful){
						val intent = Intent(applicationContext, FeedActivity::class.java)
						startActivity(intent)
						Toast.makeText(applicationContext, "Login ", Toast.LENGTH_LONG).show()
						
					}
				}.addOnFailureListener { exception ->
					Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
					
				}
	
	}
	
	
	
}
