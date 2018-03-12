package com.thinkdevs.instagram.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.thinkdevs.instagram.R
import kotlinx.android.synthetic.main.custome_view.view.*

/**
 * Created by kevinjanvier on 12/03/2018.
 */
class PostAdapter(private val userEmail:ArrayList<String>, private val userImage:ArrayList<String>,
				  private val userComment:ArrayList<String>, private val context:Activity):ArrayAdapter<String>(
		context, R.layout.custome_view, userEmail
) {
	
	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		val layoutInflater = context.layoutInflater
		val customView = layoutInflater.inflate(R.layout.custome_view, null, true)
		customView.customerUsername.text = userEmail[position]
		customView.comment.text = userComment[position]
		
		println("userComment[position] " +userComment[position])
		
		
		return customView
	}
}