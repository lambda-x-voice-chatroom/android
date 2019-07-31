package com.lambda.voicechatroom

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        context = this
        val gson = Gson()
        val userString = intent.getStringExtra(MainActivity.USER_KEY)
        val user =  gson.fromJson(userString, User::class.java)

        if (user != null) {
                edit_edituser_firstname.setText(user.firstName)
                edit_edituser_lastname.setText(user.lastName)
                edit_edituser_displayname.setText(user.displayName)
                edit_edituser_email.setText(user.email)
        }

        button_edit_profile_save.setOnClickListener {

        }


    }
}

