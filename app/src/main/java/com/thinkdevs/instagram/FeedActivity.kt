package com.thinkdevs.instagram

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.*
import com.thinkdevs.instagram.adapter.PostAdapter
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {
	var useremailFromFCM :ArrayList<String> = ArrayList<String>()
	var commentFromFCM :ArrayList<String> = ArrayList<String>()
	var imagesFromFCM :ArrayList<String> = ArrayList<String>()
	
	var firebasedb :FirebaseDatabase?= null
	var myRef:DatabaseReference?=null
	var adapter:PostAdapter?=null
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_feed)
		
		firebasedb = FirebaseDatabase.getInstance()
		myRef = firebasedb!!.getReference()
		adapter = PostAdapter(useremailFromFCM,imagesFromFCM, commentFromFCM, this)
	
		listView.adapter = adapter
		getDataFromFirebase()
	}
	
	
	fun getDataFromFirebase(){
	
		var newReference  = firebasedb!!.getReference("Post")
		newReference.addValueEventListener(object :ValueEventListener{
			
			override fun onDataChange(p0: DataSnapshot?) {
				adapter!!.clear()
				useremailFromFCM.clear()
				commentFromFCM.clear()
				imagesFromFCM.clear()
				
				for (snapshot in p0!!.children){
					val hashMap = snapshot.value as HashMap<String, String>
					if (hashMap.size> 0){
						val email  = hashMap["userEmail"]
						val comment  = hashMap["comment"]
						val image  = hashMap["download"]
						
						if (email != null){
							useremailFromFCM.add(email)
						}
						if (comment != null){
							commentFromFCM.add(comment)
						}
						if (image != null){
							imagesFromFCM.add(image)
						}
						adapter!!.notifyDataSetChanged()

					}
				}
				
				
				
				
				
			}
			
			override fun onCancelled(p0: DatabaseError?) {
			
			}
			
		})
	}
	
	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu, menu)
		return super.onCreateOptionsMenu(menu)
	}
	
	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		
		when(item!!.itemId){
			R.id.add_post->{
				val intent = Intent(applicationContext, UploadActivity::class.java)
				startActivity(intent)
			}
		}
		return super.onOptionsItemSelected(item)
	}
}
