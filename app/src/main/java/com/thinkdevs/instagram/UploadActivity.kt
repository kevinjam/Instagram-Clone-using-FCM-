package com.thinkdevs.instagram

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_upload.*
import java.util.*

class UploadActivity : AppCompatActivity() {
	var selected:Uri?=null
	var mAuth:FirebaseAuth?= null
	var mAuthListner:FirebaseAuth.AuthStateListener?= null
	var firebaseDatabase:FirebaseDatabase?=null
	var myRef:DatabaseReference?= null
	var mstorage :StorageReference?= null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_upload)
		
		mAuth = FirebaseAuth.getInstance()
		firebaseDatabase = FirebaseDatabase.getInstance()
		myRef = firebaseDatabase!!.reference
		mstorage = FirebaseStorage.getInstance().reference
	}
	
	fun Upload(view:View){
		val uuid = UUID.randomUUID()
		val imageName = "images/$uuid.jpg"
		val storageReference = mstorage!!.child(imageName)
		if (selected != null) {
			storageReference.putFile(selected!!).addOnSuccessListener { taskSnapshot ->
				val downloadUrl =taskSnapshot.downloadUrl.toString()
				println("My Download url $downloadUrl")
				
				var user = mAuth!!.currentUser
				var userEmail =user!!.email
				var userComment = comment.text.toString()
				
				var uuid = UUID.randomUUID()
				var uuidString  = uuid.toString()
				myRef!!.child("Post").child(uuidString).child("userEMail").setValue(userEmail)
				myRef!!.child("Post").child(uuidString).child("comment").setValue(userComment)
				myRef!!.child("Post").child(uuidString).child("download").setValue(downloadUrl)
			}.addOnFailureListener{exception ->
				if (exception!= null){
					Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
				}
			
			}.addOnCompleteListener{
				task ->
				if (task.isComplete){
					Toast.makeText(applicationContext, "Post Shared", Toast.LENGTH_LONG).show()
					val intent = Intent(applicationContext, FeedActivity::class.java)
					startActivity(intent)
				}
			}
		}
	
	}
	
	@RequiresApi(Build.VERSION_CODES.M)
	fun selectImage(view: View){
	
		if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){
			requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
		}else{
			val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
			startActivityForResult(intent, 2)
		}
		
		
	}
	
	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		if (requestCode == 1){
		
			if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
				val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
				startActivityForResult(intent, 2)
			}
		}
		
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
			selected = data.data
			
			try {
			
				val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selected)
				imageView.setImageBitmap(bitmap)
			
			}catch (e:Exception){
			
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data)
	}
}
