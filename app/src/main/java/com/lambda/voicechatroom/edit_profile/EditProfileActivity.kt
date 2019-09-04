package com.lambda.voicechatroom.edit_profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lambda.voicechatroom.R
import com.lambda.voicechatroom.models.User
import com.lambda.voicechatroom.groups.ViewGroupsActivity
import com.lambda.voicechatroom.login.MainActivity
import com.lambda.voicechatroom.network.ApiDao
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.coroutines.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        context = this
        val gson = Gson()
        val userString = intent.getStringExtra(MainActivity.USER_KEY)
        val user = gson.fromJson(userString, User::class.java)

        edit_edituser_firstname.setText(user.firstName)
        edit_edituser_lastname.setText(user.lastName)
        edit_edituser_displayname.setText(user.displayName)
        edit_edituser_email.setText(user.email)


        button_edit_profile_save.setOnClickListener {
            progress_edit_profile.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO + Job()).launch {
                user.firstName = edit_edituser_firstname.text.toString()
                user.lastName = edit_edituser_lastname.text.toString()
                user.displayName = edit_edituser_displayname.text.toString()
                user.email = edit_edituser_email.text.toString()
                if (ApiDao.updateUser(user)) {
                    withContext(Dispatchers.Main) {
                        progress_edit_profile.visibility = View.GONE
                        startActivity(Intent(context, ViewGroupsActivity::class.java))
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        progress_edit_profile.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Something went wrong.  Check network connection and try again.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}

